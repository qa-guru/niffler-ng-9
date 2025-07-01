package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();
  private final String userName = "testUser2";
  private final String password = "23456";
  private final LoginPage loginPage = new LoginPage();

  @BeforeEach
  void setUp() {
    Selenide.open(CFG.frontUrl(), LoginPage.class);
  }

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    loginPage
        .fillLoginPage(userName, password)
        .submit()
        .checkThatPageLoaded();
  }

  @DisplayName("Пользователь должен перейти на главную страницы после успешной авторизации")
  @Test
  void mainPageShouldBeVisibleAfterSuccessLogin() {
    loginPage
            .fillLoginPage(userName, password)
            .submit()
            .verifyLoaded();
  }

  @DisplayName("Пользователь должен остаться на странице авторизации после неуспешной авторизации")
  @Test
  void shouldStayOnLoginPageAfterUnsuccessfulLogin() {
    String password = "1234561";
    loginPage
            .fillLoginPage(userName, password)
            .submit();
    loginPage.verifyErrorBlockVisible();
  }
}
