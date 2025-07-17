package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.userdata.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserJson createUser(AuthUserJson json) {
        return transaction(
            connection -> {
              UserEntity ue = UserEntity.fromJson(json);
              ue.setPassword(pe.encode(json.password()));
              return AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).createAuthUser(ue));
              }, CFG.authJdbcUrl()
        );
  }

  public Optional<AuthUserJson> findById(AuthUserJson json) {
    return transaction(
        connection -> {
          Optional<UserEntity> ue = new AuthUserDaoJdbc(connection)
              .findById(json.id());
          return ue.map(AuthUserJson::fromEntity);
        }, CFG.authJdbcUrl()
    );
  }

  public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
    return transaction(
        connection -> {
          Optional<UserEntity> ue = new AuthUserDaoJdbc(connection)
              .findByUsername(json.username());
          return ue.map(AuthUserJson::fromEntity);
        }, CFG.authJdbcUrl()
    );
  }

  public void deleteUser(AuthUserJson json) {
    transaction(
        connection -> {
          UserEntity ue = UserEntity.fromJson(json);
          new AuthUserDaoJdbc(connection).deleteUser(ue);
        }, CFG.authJdbcUrl()
    );
  }

  public UserJson createUserSpringJdbc(UserJson user) {
    UserEntity authUser = new UserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    UserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
        .createAuthUser(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
        e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUserId(createdAuthUser.getId());
          ae.setAuthority(e);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
        .create(authorityEntities);

    return UserJson.fromEntity(
        new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
            .create(
                UdUserEntity.fromJson(user)
            )
    );
  }
}
