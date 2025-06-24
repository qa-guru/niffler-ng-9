package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.type;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement
      usernameInput = $("input[name='username']"),
      passwordInput = $("input[name='password']"),
      submitButton = $("button[type='submit']"),
      loginForm = $("#login-form"),
      registrationBtn = $("#register-button"),
      formError = $("#login-form p");

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public LoginPage submitAndCheckErrorText(String error){
    submitButton.click();
    formError.shouldHave(text(error));
    return this;
  }

  public LoginPage showPassword(){
    loginForm.$$("label").find(text("Password"))
            .$("button").click();
    return this;
  }

  public LoginPage checkPasswordInputType(){
    loginForm.$$("label").find(text("Password"))
            .$("input").shouldHave(type("text"));
    return this;
  }

  public RegistrationPage openRegistrationForm(){
    registrationBtn.click();
    return new RegistrationPage();
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }
}
