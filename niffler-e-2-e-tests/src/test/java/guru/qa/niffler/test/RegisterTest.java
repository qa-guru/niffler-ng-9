package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {

  private static final Config CFG = Config.getInstance();
  Faker faker = new Faker();
  String password = "12345";

  @Test
  @DisplayName("Register new user")
  void shouldRegisterNewUser(){
    var usernsme = faker.name().username();
    System.out.println(usernsme);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doRegister()
            .fillRegisterForm(usernsme, password,password)
            .submitRegistration()
            .checkCongratulationsMessage()
            .singIn()
            .doLogin(usernsme, password)
            .checkThatMainPageStatistics();
  }

  @Test
  @DisplayName("Can not register user with the same username")
  void shouldNotRegisterUserWithExistingUsername(){
    String error = String.format("Username `%s` already exists", "duck");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doRegister()
            .fillRegisterForm("duck", password,password)
            .submitRegistration()
            .checkErrorMessage();

    assertEquals(error, new RegisterPage().getErrorMessage());
  }

  @Test
  @DisplayName("Passwords should be equal")
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
    var usernsme = faker.name().username();
    String error = "Passwords should be equal";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doRegister()
            .fillRegisterForm(usernsme, password,"123456")
            .submitRegistration();

    assertEquals(error, new RegisterPage().getErrorMessage());
  }

}
