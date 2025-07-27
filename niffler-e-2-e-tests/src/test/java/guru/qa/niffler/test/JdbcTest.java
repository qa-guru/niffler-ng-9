package guru.qa.niffler.test;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.*;

public class JdbcTest {

  private final SpendDbClient spendDbClient = new SpendDbClient();
  private final AuthDbClient authDbClient = new AuthDbClient();
  private final UserdataDbClient userdataDbClient = new UserdataDbClient();

  @Test
  void authRepositoryShouldReturnAllUsersWithAuthorities() {
    List<AuthUserEntity> users = authDbClient.findAll();
    System.out.println(users);
  }

  @Test
  void userShouldBeCreatedWithAuthorities() {
    AuthUserJson json = new AuthUserJson(
        UUID.randomUUID(),
        "test934",
        "12345",
        true,
        true,
        true,
        true,
        new ArrayList<>()
    );
    AuthUserJson ue = authDbClient.createUser(json);
    System.out.println(ue);
  }

  @Test
  void findByIdShouldReturnUserWithAllAuthorities() {
    AuthUserJson json = new AuthUserJson(
        UUID.fromString("c9cb463e-8b90-402b-9240-5b4f39c7ffec"),
        "test93",
        "12345",
        true,
        true,
        true,
        true,
        new ArrayList<>()
    );
    Optional<AuthUserJson> ue = authDbClient.findById(json);
    System.out.println(ue);
  }

  @Test
  void findAllShouldReturnAllUsersWithAllAuthorities() {
    List<AuthUserEntity> ue = authDbClient.findAll();
    System.out.println(ue);
  }

  @Test
  void findAllShouldReturnAllAuthoritiesSpring() {
    List<AuthorityEntity> ae = authDbClient.findAllAuthorities();
    System.out.println(ae);
  }

  @Test
  void findByUserIdShouldReturnListOfUserAuthorities() {
    List<AuthorityEntity> ae = authDbClient.findAuthoritiesByUserId(UUID.fromString("c9cb463e-8b90-402b-9240-5b4f39c7ffec"));
    System.out.println(ae);
  }

  @Test
  void udFindByIdMethodShouldReturnAllFriendships() {
    Optional<UdUserJson> ue = userdataDbClient.findById(UUID.fromString("1579929d-e4c0-4fd7-9870-684e5f426535"));
    System.out.println(ue);
  }

  @Test
  void udFindByUsernameMethodShouldReturnAllFriendships() {
    Optional<UdUserEntity> ue = userdataDbClient.findByUsername("test1");
    System.out.println(ue);
  }

  @Test
  void udFindAllMethodShouldReturnAllUsersWithFriendships() {
    List<UdUserEntity> ue = userdataDbClient.findAll();
    System.out.println(ue);
  }

  @Test
  void incomeFriendshipShouldBeCreatedSpring() {
    UdUserJson requester = new UdUserJson(
            UUID.fromString("9828191e-2e48-4b3c-a372-c782601e2476"),
            randomUsername(),
            RUB,
            randomName() + randomSurname(),
            randomName(),
            randomSurname(),
            new byte[]{},
            new byte[]{}
    );
    UdUserJson addressee = new UdUserJson(
            UUID.fromString("1579929d-e4c0-4fd7-9870-684e5f426535"),
            randomUsername(),
            RUB,
            randomName() + randomSurname(),
            randomName(),
            randomSurname(),
            new byte[]{},
            new byte[]{}
    );
    userdataDbClient.sendInvitation(requester, addressee);
  }

  @Test
  void categoryCreationJdbcTest() {
    CategoryJson json = spendDbClient.createCategory(
        new CategoryJson(
            UUID.randomUUID(),
            "Test category 123",
            "test1",
            false
        )
    );
    System.out.println(json);
  }

  @Test
  void chainedTxUserCreationJdbcTest() {
    UdUserJson user = authDbClient.createUserChainedTx(
        new UdUserJson(
            null,
            "valentin-8",
            RUB,
            null,
            null,
            null,
            null,
            null
        )
    );
    System.out.println(user);
  }
}
