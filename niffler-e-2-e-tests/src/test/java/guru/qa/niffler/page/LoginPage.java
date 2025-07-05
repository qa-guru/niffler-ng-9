package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.Pages.mainPage;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewUserButton = $("#register-button");
  private final SelenideElement registerButton = $("a[href='/register']");
  private final SelenideElement errorContainer = $(".form__error");


  public RegisterPage doRegister() {
    registerButton.click();
    return new RegisterPage();
  }

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return mainPage;
  }

  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }

  public RegisteredPage createNewUserButton(){
    createNewUserButton.click();
    return new RegisteredPage();
  }

  public MainPage successLogin(String username, String password) {
    fillLoginPage(username, password);
    submit();
    return mainPage;
  }

  public LoginPage checkFailedLogin() {
    $x("//*[text() = 'Неверные учетные данные пользователя']").shouldBe(visible);
    return this;
  }
}