package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

  private static final Config CFG = Config.getInstance();

  String username = new Faker().name().username();

  @Test
  @DisplayName("Main page should be present after successful login")
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage("duck", "12345")
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  @DisplayName("Unregistered user should get error when trying to login")
  void errorShouldBeShownIfUserNotRegistered(){
    final String errorText = "Неверные учетные данные пользователя";

    open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(username, "12345")
            .submitAndCheckErrorText(errorText);
  }

  @Test
  @DisplayName("Error should be present if user trying to login with incorrect password")
  void errorShouldBeShownWithIncorrectRegisteredUserPassword(){
    final String errorText = "Неверные учетные данные пользователя";

    open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("test1", new Faker().internet().password())
            .submitAndCheckErrorText(errorText);
  }

  @Test
  @DisplayName("Password should be visible after changing visibility")
  void passwordShouldBeShownAfterVisibilityChanging(){
    open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage(username, "12345")
            .showPassword()
            .checkPasswordInputType();
  }
}
