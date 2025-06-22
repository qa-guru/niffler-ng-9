package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTest {

  private static final Config CFG = Config.getInstance();

  String username = new Faker().name().username();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage("duck", "12345")
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  void errorShouldBeShownIfUserNotRegistered(){
    final String errorText = "Неверные учетные данные пользователя";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(username, "12345")
            .submitAndCheckErrorText(errorText);
  }

  //TODO registration of user before test
  @Test
  void errorShouldBeShownWithIncorrectRegisteredUserPassword(){

  }

  @Test
  void passwordShouldBeShownAfterVisibilityChanging(){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(username, "12345")
            .showPassword()
            .checkPasswordInputType();
  }
}
