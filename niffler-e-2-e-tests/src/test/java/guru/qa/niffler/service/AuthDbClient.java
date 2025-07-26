package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthAuthorityRepository;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.spring.AuthAuthorityRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.spring.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();
  private final AuthUserDao authUserSpringDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
  private final AuthAuthorityDao authoritySpringDao = new AuthAuthorityDaoSpringJdbc();
  private final AuthAuthorityRepository authorityRepository = new AuthAuthorityRepositorySpringJdbc();
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
  public UdUserJson createUserChainedTx(UdUserJson json) {
    return chainedTxTemplate.execute(status -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(json.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          AuthUserEntity createdAuthUser = authUserRepository.create(authUser);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
              e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUser(createdAuthUser);
                ae.setAuthority(e);
                return ae;
              }
          ).toArray(AuthorityEntity[]::new);

          authorityDao.create(authorityEntities);

          return UdUserJson.fromEntity(
              userdataUserDao.create(UdUserEntity.fromJson(json))
          );
        }
    );
  }

  public AuthUserJson createUserSpringJdbc(AuthUserJson json) {
    AuthUserEntity ue = AuthUserEntity.fromJson(json);
    return AuthUserJson.fromEntity(authUserSpringDao.createAuthUser(ue));
  }

  public AuthUserJson createUser(AuthUserJson json) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity ue = AuthUserEntity.fromJson(json);
          ue.setPassword(pe.encode(json.password()));
          return AuthUserJson.fromEntity(authUserRepository.create(ue));
        }
    );
  }

  public Optional<AuthUserJson> findByIdSpringJdbc(AuthUserJson json) {
    Optional<AuthUserEntity> ue = authUserSpringDao.findById(json.id());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findById(AuthUserJson json) {
    Optional<AuthUserEntity> ue = authUserRepository.findById(json.id());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findByUsernameSpringJdbc(AuthUserJson json) {
    Optional<AuthUserEntity> ue = authUserSpringDao.findByUsername(json.username());
    return ue.map(AuthUserJson::fromEntity);
  }

  public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
    Optional<AuthUserEntity> ue = authUserRepository.findByUsername(json.username());
    return ue.map(AuthUserJson::fromEntity);
  }

  public List<AuthUserEntity> findAll() {
    return authUserRepository.findAll();
  }

  public List<AuthorityEntity> findAllAuthorities() {
    return authorityRepository.findAll();
  }

  public List<AuthorityEntity> findAuthoritiesByUserId(UUID id) {
    return authorityRepository.findByUserId(id);
  }

  public void deleteUserSpringJdbc(AuthUserJson json) {
    AuthUserEntity ue = AuthUserEntity.fromJson(json);
    authUserSpringDao.deleteUser(ue);
  }

  public void deleteUser(AuthUserJson json) {
    AuthUserEntity ue = AuthUserEntity.fromJson(json);
    authUserRepository.delete(ue);

  }

  public UdUserJson createUserSpringJdbc(UdUserJson user) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          AuthUserEntity createdAuthUser = authUserSpringDao.createAuthUser(authUser);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
              e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUser(createdAuthUser);
                ae.setAuthority(e);
                return ae;
              }
          ).toArray(AuthorityEntity[]::new);

          authoritySpringDao.create(authorityEntities);

          return UdUserJson.fromEntity(
              userdataUserSpringDao.create(UdUserEntity.fromJson(user))
          );
        }
    );
  }
}
