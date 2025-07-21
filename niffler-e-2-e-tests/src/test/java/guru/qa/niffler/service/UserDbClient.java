package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.userdata.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthUserDao authUserSpringDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
  private final AuthAuthorityDao authoritySpringDao = new AuthAuthorityDaoSpringJdbc();
  private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();
  private final UserdataUserDao userdataUserSpringDao = new UserdataUserDaoSpringJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.authJdbcUrl()
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  private final TransactionTemplate chainedTxTemplate = new TransactionTemplate(
      new ChainedTransactionManager(
          new JdbcTransactionManager(
              DataSources.dataSource(CFG.authJdbcUrl())
          ),
          new JdbcTransactionManager(
              DataSources.dataSource(CFG.userdataJdbcUrl())
          )
      )
  );

  //Works well only with Spring JDBC. Classic JDBC operations are non-consistent.
  public UserJson createUserChainedTx(UserJson json) {
    return chainedTxTemplate.execute(status -> {
          UserEntity authUser = new UserEntity();
          authUser.setUsername(json.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          UserEntity createdAuthUser = authUserDao.createAuthUser(authUser);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
              e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUserId(createdAuthUser.getId());
                ae.setAuthority(e);
                return ae;
              }
          ).toArray(AuthorityEntity[]::new);

          authorityDao.create(authorityEntities);

          return UserJson.fromEntity(
              userdataUserDao.create(UdUserEntity.fromJson(json))
          );
        }
    );
  }

  public AuthUserJson createUserSpringJdbc(AuthUserJson json) {
    UserEntity ue = UserEntity.fromJson(json);
    return AuthUserJson.fromEntity(authUserSpringDao.createAuthUser(ue));
  }

  public AuthUserJson createUser(AuthUserJson json) {
    UserEntity ue = UserEntity.fromJson(json);
    ue.setPassword(pe.encode(json.password()));
    return AuthUserJson.fromEntity(authUserDao.createAuthUser(ue));
  }

  public Optional<AuthUserJson> findByIdSpringJdbc(AuthUserJson json) {
    Optional<UserEntity> ue = authUserSpringDao.findById(json.id());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findById(AuthUserJson json) {
    Optional<UserEntity> ue = authUserDao.findById(json.id());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findByUsernameSpringJdbc(AuthUserJson json) {
    Optional<UserEntity> ue = authUserSpringDao.findByUsername(json.username());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
    Optional<UserEntity> ue = authUserDao.findByUsername(json.username());
    return ue.map(AuthUserJson::fromEntity);
  }

  public void deleteUserSpringJdbc(AuthUserJson json) {
    UserEntity ue = UserEntity.fromJson(json);
    authUserSpringDao.deleteUser(ue);
  }

  public void deleteUser(AuthUserJson json) {
    UserEntity ue = UserEntity.fromJson(json);
    authUserDao.deleteUser(ue);

  }

  public UserJson createUserSpringJdbc(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
          UserEntity authUser = new UserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          UserEntity createdAuthUser = authUserSpringDao.createAuthUser(authUser);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
              e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUserId(createdAuthUser.getId());
                ae.setAuthority(e);
                return ae;
              }
          ).toArray(AuthorityEntity[]::new);

          authoritySpringDao.create(authorityEntities);

          return UserJson.fromEntity(
              userdataUserSpringDao.create(UdUserEntity.fromJson(user))
          );
        }
    );
  }
}
