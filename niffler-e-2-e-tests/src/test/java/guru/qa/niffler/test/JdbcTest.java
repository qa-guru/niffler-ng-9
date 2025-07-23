package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.UserdataDbClient;
import org.junit.jupiter.api.Test;

import java.util.*;

import static guru.qa.niffler.model.CurrencyValues.*;

public class JdbcTest {

  private final SpendDbClient spendDbClient = new SpendDbClient();
  private final AuthDbClient authDbClient = new AuthDbClient();
  private final UserdataDbClient userdataDbClient = new UserdataDbClient();


  @Test
  void spendCreationSpringJdbcTest() {
    SpendJson json = spendDbClient.createSpendSpringJdbc(
        new SpendJson(
            UUID.randomUUID(),
            new Date(),
            new CategoryJson(
                UUID.fromString("bd79d3f7-1004-44ec-a69f-270974b6f585"),
                null,
                null,
                false
            ),
            RUB,
            101.01,
            "Test",
            "test1"
        )
    );
    System.out.println(json);
  }

  @Test
  void authRepositoryShouldReturnAllUsersWithAuthorities() {
    List<AuthUserEntity> users = authDbClient.findAll();
    System.out.println(users);
  }

  @Test
  void userShouldBeCreatedWithAuthorities() {
    AuthUserJson json = new AuthUserJson(
        UUID.randomUUID(),
        "test93",
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
    Optional<UdUserEntity> ue = userdataDbClient.findById(UUID.fromString("1579929d-e4c0-4fd7-9870-684e5f426535"));
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
    UdUserEntity requester = new UdUserEntity();
    requester.setId(UUID.fromString("9828191e-2e48-4b3c-a372-c782601e2476"));
    UdUserEntity addressee = new UdUserEntity();
    addressee.setId(UUID.fromString("1579929d-e4c0-4fd7-9870-684e5f426535"));
    userdataDbClient.createIncomeFriendshipInvitation(requester, addressee);
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
  void deleteCategorySpringJdbcTest () {
    CategoryJson json = spendDbClient.createCategory(
        new CategoryJson(
            UUID.randomUUID(),
            new Faker().funnyName().name(),
            "test1",
            false
        )
    );
    System.out.println(json);
    spendDbClient.deleteCategorySpringJdbc(json);
  }

  @Test
  void categoryCreationSpringJdbcTest() {
    CategoryJson categoryJson = spendDbClient.createCategorySpringJdbc(
        new CategoryJson(
            UUID.randomUUID(),
            "Test Cat",
            "test1",
            false
        )
    );
    System.out.println(categoryJson);
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
