package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class LoginTest {

  private static final Config CFG = Config.getInstance();
  private LoginPage loginPage = new LoginPage();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    loginPage.openPage()
        .fillLoginPage("duck", "12345")
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterBadCredentials() {
    loginPage.openPage()
            .fillLoginPage("1", "1")
            .submit();
    loginPage.checkInvalidUserAndPasswordError();
  }

  @AfterAll
  static void suiteTearDown() {
    Selenide.closeWebDriver();
  }
}
