package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.*;

public class JdbcTest {

  private final SpendDbClient spendDbClient = new SpendDbClient();
  private final UserDbClient usersDbClient = new UserDbClient();


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
    List<AuthUserEntity> users = usersDbClient.findAll();
    System.out.println(users);
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
    UdUserJson user = usersDbClient.createUserChainedTx(
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
