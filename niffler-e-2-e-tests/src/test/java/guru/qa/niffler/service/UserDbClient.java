package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.userdata.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authorityDao = new AuthAuthorityDaoSpringJdbc();
  private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.authJdbcUrl()
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  public AuthUserJson createUser(AuthUserJson json) {
    return jdbcTxTemplate.execute(
        () -> {
          UserEntity ue = UserEntity.fromJson(json);
          ue.setPassword(pe.encode(json.password()));
          return AuthUserJson.fromEntity(authUserDao.createAuthUser(ue));
        }
    );
  }

  public Optional<AuthUserJson> findById(AuthUserJson json) {
    return jdbcTxTemplate.execute(
        () -> {
          Optional<UserEntity> ue = authUserDao.findById(json.id());
          return ue.map(AuthUserJson::fromEntity);
        }
    );
  }

  public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
    return jdbcTxTemplate.execute(
        () -> {
          Optional<UserEntity> ue = authUserDao.findByUsername(json.username());
          return ue.map(AuthUserJson::fromEntity);
        }
    );
  }

  public void deleteUser(AuthUserJson json) {
    jdbcTxTemplate.execute(
        () -> {
          UserEntity ue = UserEntity.fromJson(json);
          authUserDao.deleteUser(ue);
          return null;
        }
    );
  }

  public UserJson createUserSpringJdbc(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
          UserEntity authUser = new UserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(null);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          UserEntity createdAuthUser = authUserDaoSpring.createAuthUser(authUser);

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
              userdataUserDao.create(UdUserEntity.fromJson(user))
          );
        }
    );
  }
}
