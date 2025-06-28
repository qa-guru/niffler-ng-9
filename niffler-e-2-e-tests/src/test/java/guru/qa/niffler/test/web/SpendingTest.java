package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(
          username = "duck",
          spending = @Spending(
                  amount = 8990.00,
                  description = "norandom Desc!",
                  category = "Учёба"
          )
  )
  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(SpendJson spendJson) {
    final String newDescription = ":)";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("duck", "12345")
        .checkThatPageLoaded()
        .editSpending(spendJson.description())
        .setNewSpendingDescription(newDescription)
        .save()
        .checkThatTableContainsSpending(newDescription);
  }
}
