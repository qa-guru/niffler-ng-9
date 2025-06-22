package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTest {

  private static final Config CFG = Config.getInstance();
  private static final String correctLogin = "duck";
  private static final String correctPassword = "12345";
  private static final String uncorrectPassword = "123456";

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(correctLogin, correctPassword)
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  void userGetBadCredentialsErrorMessageWithWrongPassword() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(correctLogin, uncorrectPassword)
            .clickSubmitButton()
            .checkBadCredentialsErrorMessage();
  }
}
