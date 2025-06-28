package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("duck", "12345")
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    loginPage.fillLoginPage(RandomDataUtils.randomUserName(),"BAD");
    loginPage.checkError("Неверные учетные данные пользователя");
  }
}
