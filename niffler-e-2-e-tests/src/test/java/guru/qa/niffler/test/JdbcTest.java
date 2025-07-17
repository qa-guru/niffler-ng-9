package guru.qa.niffler.test;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.*;

public class JdbcTest {
  @Test
  void spendSpringJdbcTest(){
    SpendDbClient spendDbClient = new SpendDbClient();

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
  void springJdbcTest() {
    UserDbClient usersDbClient = new UserDbClient();
    UserJson user = usersDbClient.createUserSpringJdbc(
        new UserJson(
            null,
            "valentin-7",
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
