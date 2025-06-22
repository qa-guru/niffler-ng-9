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

  private static final String username = "duck";
  private static final String password = "12345";

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(username, password)
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(username, username)
            .checkErrorAfterSubmitWithBadCredentials();
  }
}
