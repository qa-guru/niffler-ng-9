package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createAccountButton = $("#register-button");
  private final SelenideElement formError = $(".form__error");

  private static final String ERROR_BAD_CREDENTIALS = "Bad credentials";

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }

  public LoginPage submitWithBadCredentials() {
    submitButton.click();
    return new LoginPage();
  }

  public void checkErrorBadCredentials() {
    formError.should(visible);
    formError.shouldHave(text(ERROR_BAD_CREDENTIALS));
  }

  public RegisterPage createNewAccount() {
    createAccountButton.click();
    return new RegisterPage();
  }
}
