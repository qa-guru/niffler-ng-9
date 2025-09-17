package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;


public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();


    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    public UserJson createUserSpringJdbc(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(authorityEntities);
            return UserJson.fromEntity(
                    udUserDao.create(UserEntity.fromJson(user)),
                    null
            );
        });
    }

    public UserJson createUserWithoutSpringJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword("12345");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(
                Authority.values()
        ).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDao.create(UserEntity.fromJson(user)), null
        );
    }

    public UserJson createUserJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(null);
                    authUser.setPassword("12345");
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);
                    authAuthorityDao.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user)), null
                    );
                }
        );
    }

    public UserJson createUserWithoutJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword("12345");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDao.create(UserEntity.fromJson(user)), null
        );
    }


//    public UserJson createUserSpringJdbc(UserJson user) {
//        AuthUserEntity authUser = new AuthUserEntity();
//        authUser.setUsername(user.username());
//        authUser.setPassword(pe.encode("12345"));
//        authUser.setEnabled(true);
//        authUser.setAccountNonExpired(true);
//        authUser.setAccountNonLocked(true);
//        authUser.setCredentialsNonExpired(true);
//
//        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
//                .create(authUser);
//
//        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
//                e -> {
//                    AuthorityEntity ae = new AuthorityEntity();
//                    ae.setUserId(createdAuthUser.getId());
//                    ae.setAuthority(e);
//                    return ae;
//                }
//        ).toArray(AuthorityEntity[]::new);
//
//        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
//                .create(authorityEntities);
//
//        return UserJson.fromEntity(
//                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
//                        .create(
//                                UserEntity.fromJson(user)
//                        ),
//                null
//        );
//    }
//
//    public UserJson createUser(UserJson userJson, int transactionLevel) {
//        return UserJson.fromEntity(
//                xaTransaction(
//                        transactionLevel,
//                        new Databases.XaFunction<>(
//                                connection -> {
//                                    AuthUserEntity authUser = new AuthUserEntity();
//                                    authUser.setUsername(userJson.username());
//                                    authUser.setPassword(pe.encode("052322"));
//                                    authUser.setEnabled(false);
//                                    authUser.setAccountNonExpired(false);
//                                    authUser.setAccountNonLocked(false);
//                                    authUser.setCredentialsNonExpired(false);
//                                    new AuthUserDaoJdbc(connection).create(authUser);
//                                    new AuthAuthorityDaoJdbc(connection).create(
//                                            Arrays.stream(Authority.values())
//                                                    .map(authority -> {
//                                                                AuthorityEntity authorityEntity = new AuthorityEntity();
//                                                                authorityEntity.setUserId(authUser.getId());
//                                                                authorityEntity.setAuthority(authority);
//                                                                return authorityEntity;
//                                                            }
//                                                    ).toArray(AuthorityEntity[]::new));
//                                    return null;
//                                },
//                                CFG.authJdbcUrl()
//                        ),
//                        new Databases.XaFunction<>(
//                                connection -> {
//                                    UserEntity userEntity = new UserEntity();
//                                    userEntity.setUsername(userJson.username());
//                                    userEntity.setFullname(userJson.fullname());
//                                    userEntity.setCurrency(userJson.currency());
//                                    new UserdataUserDaoJdbc(connection).create(userEntity);
//                                    return userEntity;
//                                },
//                                CFG.userdataJdbcUrl()
//                        )
//                ),
//                null);
//    }
//
//    public Optional<UserEntity> findById(UUID id, int transactionLevel) {
//        return transaction(connection -> {
//            Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connection)
//                    .findById(id);
//            return userEntity;
//        },
//                CFG.userdataJdbcUrl(),
//                transactionLevel
//        );
//    }
//
//    public Optional<UserEntity> findByUserName(String username, int transactionLevel) {
//        return transaction(connection -> {
//                    Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connection)
//                            .findByUserName(username);
//                    return userEntity;
//                },
//                CFG.userdataJdbcUrl(),
//                transactionLevel
//        );
//    }
//
//    public Boolean delete(UserJson user, int transactionLevel) {
//        return transaction(connection -> {
//                    UserEntity userEntity = UserEntity.fromJson(user);
//                    Boolean deletedUser = new UserdataUserDaoJdbc(connection)
//                            .delete(userEntity);
//                    return deletedUser;
//                },
//                CFG.userdataJdbcUrl(),
//                transactionLevel
//        );
//    }
}
