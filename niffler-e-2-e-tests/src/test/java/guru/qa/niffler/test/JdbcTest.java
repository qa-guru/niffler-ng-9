package guru.qa.niffler.test;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@Disabled
public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );

        System.out.println(spend);
    }

    @Test
    void authUserTxTest() {
        UserDbClient userDbClient = new UserDbClient();

        UserJson user = new UserJson(
                null,
                "fox",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );

        String errorMessage = "";
        try {
            userDbClient.createUserSpringJdbcViaTx(user);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }

        assertThat(errorMessage, containsString("Key (username)=(fox) already exists."));
        System.out.println(user);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "wombat",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        String errorMessage = "";
        try {
            UserJson createdUser = usersDbClient.createUserSpringJdbcViaTx(user);
            System.out.println("Created User: " + createdUser);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }
        assertThat(errorMessage, containsString("Key (username)=(wombat) already exists."));
    }

    @Test
    void chainedTxTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "whale",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserViaChainedTx(user);
        System.out.println("Created User: " + createdUser);
    }

    @Test
    void createUserSpringJdbcWithoutTx() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "falcon",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserSpringJdbcWithoutTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserSpringJdbcViaTx() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "bear",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserSpringJdbcViaTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserJdbcWithoutTxTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "beaver",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserJdbcWithoutTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserJdbcViaTxTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "squirrel",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserJdbcViaTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserViaJdbcRepository() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "repoUser_1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUser(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserViaSpringRepository() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = new UserJson(
                null,
                "Spring Repo User 1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserViaRepositorySpring(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void findUserById() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.findUserById(UUID
                .fromString("d149c9c5-f8a0-42fa-823e-c0d9f9f94797"));
        System.out.println(user);
    }

    @Test
    void addFriendshipViaRepositoryJdbcTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserJson requesterUser = new UserJson(
                UUID.fromString("6998e039-d10d-4dbd-a6a5-df64b220e2cb"),
                "dove",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson addresseeUser = new UserJson(
                UUID.fromString("30fbb230-8076-4c9f-b210-13750ea0c2db"),
                "goose",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        userDbClient.createFriendshipRepositoryJdbc(requesterUser, addresseeUser);
    }

    @Test
    void addFriendshipViaRepositorySpringTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserJson requesterUser = new UserJson(
                UUID.fromString("b815b120-6eaf-11f0-bd0f-0242ac110002"),
                "Spring Repo User",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson addresseeUser = new UserJson(
                UUID.fromString("8f6d8e58-6f01-11f0-b0eb-0242ac110002"),
                "repoUser_1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        userDbClient.createFriendshipRepositorySpring(requesterUser, addresseeUser);
    }

    @Test
    void findAllTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        spendDbClient.findAll().forEach(System.out::println);
    }
}