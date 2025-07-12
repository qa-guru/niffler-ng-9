package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.auth.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUser(UserJson userJson, int transactionLevel) {
        return UserJson.fromEntity(
                xaTransaction(
                        transactionLevel,
                        new Databases.XaFunction<>(
                                connection -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(userJson.username());
                                    authUser.setPassword(pe.encode("052322"));
                                    authUser.setEnabled(false);
                                    authUser.setAccountNonExpired(false);
                                    authUser.setAccountNonLocked(false);
                                    authUser.setCredentialsNonExpired(false);
                                    new AuthUserDaoJdbc(connection).create(authUser);
                                    new AuthAuthorityDaoJdbc(connection).create(
                                            Arrays.stream(Authority.values())
                                                    .map(authority -> {
                                                                AuthorityEntity authorityEntity = new AuthorityEntity();
                                                                authorityEntity.setUserId(authUser.getId());
                                                                authorityEntity.setAuthority(authority);
                                                                return authorityEntity;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new Databases.XaFunction<>(
                                connection -> {
                                    UserEntity userEntity = new UserEntity();
                                    userEntity.setUsername(userJson.username());
                                    userEntity.setFullname(userJson.fullname());
                                    userEntity.setCurrency(userJson.currency());
                                    new UserdataUserDaoJdbc(connection).createUser(userEntity);
                                    return userEntity;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ),
                null);
    }

    public Optional<UserEntity> findById(UUID id, int transactionLevel) {
        return transaction(connection -> {
            Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connection)
                    .findById(id);
            return userEntity;
        },
                CFG.userdataJdbcUrl(),
                transactionLevel
        );
    }

    public Optional<UserEntity> findByUserName(String username, int transactionLevel) {
        return transaction(connection -> {
                    Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connection)
                            .findByUserName(username);
                    return userEntity;
                },
                CFG.userdataJdbcUrl(),
                transactionLevel
        );
    }

    public Boolean delete(UserJson user, int transactionLevel) {
        return transaction(connection -> {
                    UserEntity userEntity = UserEntity.fromJson(user);
                    Boolean deletedUser = new UserdataUserDaoJdbc(connection)
                            .delete(userEntity);
                    return deletedUser;
                },
                CFG.userdataJdbcUrl(),
                transactionLevel
        );
    }
}
