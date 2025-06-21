package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccountButton = $("#register-button");
  private final SelenideElement errorMessage = $("p.form__error");

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage createNewAccount() {
    createNewAccountButton.click();
    return new RegisterPage();
  }

  public void submitAndCheckErrorInvalidCredentials() {
    submitButton.click();
    String errorMessage = this.errorMessage.should(visible).text();
    Assertions.assertEquals("Неверные учетные данные пользователя", errorMessage);
  }
}
