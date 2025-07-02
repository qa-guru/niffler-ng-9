package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @DisplayName("Main Page is displayed after login")
  public void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .checkThatMainPageLoaded();
  }

  @Test
  @DisplayName("User Stay On Login Page After Login With Bad Credentials")
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("duck", "123456");
    String errorMessage = new LoginPage().getErrorMessage();

    Assertions.assertEquals("Bad credentials", errorMessage);
  }

}
