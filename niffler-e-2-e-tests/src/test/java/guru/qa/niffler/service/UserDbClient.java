package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserJson createUser(AuthUserJson json) {
        return transaction(
            connection -> {
              AuthUserEntity ue = AuthUserEntity.fromJson(json);
              ue.setPassword(pe.encode(json.password()));
              return AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).createAuthUser(ue));
              }, CFG.authJdbcUrl()
        );
  }

  public Optional<AuthUserJson> findById(AuthUserJson json) {
    return transaction(
        connection -> {
          Optional<AuthUserEntity> ue = new AuthUserDaoJdbc(connection)
              .findById(json.id());
          return ue.map(AuthUserJson::fromEntity);
        }, CFG.authJdbcUrl()
    );
  }

  public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
    return transaction(
        connection -> {
          Optional<AuthUserEntity> ue = new AuthUserDaoJdbc(connection)
              .findByUsername(json.username());
          return ue.map(AuthUserJson::fromEntity);
        }, CFG.authJdbcUrl()
    );
  }

  public void deleteUser(AuthUserJson json) {
    transaction(
        connection -> {
          AuthUserEntity ue = AuthUserEntity.fromJson(json);
          new AuthUserDaoJdbc(connection).deleteUser(ue);
        }, CFG.authJdbcUrl()
    );
  }
}
