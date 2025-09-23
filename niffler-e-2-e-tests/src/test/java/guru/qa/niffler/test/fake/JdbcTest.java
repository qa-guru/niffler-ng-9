package guru.qa.niffler.test.fake;

import guru.qa.niffler.jupiter.extension.ClientResolver;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

@Disabled
@ExtendWith(ClientResolver.class)
public class JdbcTest {

  private SpendClient spendClient;
  private UsersClient usersClient;

  @Test
  void txTest() {
    SpendJson spend = spendClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-4",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx-4",
            "duck"
        )
    );

    System.out.println(spend);
  }


  @ValueSource(strings = {
      "valentin-12"
  })
  @ParameterizedTest
  void springJdbcTest(String uname) {
    UserJson user = usersClient.createUser(
        uname,
        "12345"
    );

    usersClient.addIncomeInvitation(user, 1);
    usersClient.addOutcomeInvitation(user, 1);
    usersClient.addFriend(user, 1);
  }
}
