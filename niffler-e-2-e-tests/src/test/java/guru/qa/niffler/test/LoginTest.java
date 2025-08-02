package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();
  private static final String CORRECT_USERNAME = "duck";
  private static final String WRONG_USERNAME = "donalduck";
  private static final String CORRECT_PASSWORD = "12345";

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(CORRECT_USERNAME, CORRECT_PASSWORD)
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(WRONG_USERNAME, CORRECT_PASSWORD)
            .submitWithBadCredentials()
            .checkErrorBadCredentials();
  }
}
