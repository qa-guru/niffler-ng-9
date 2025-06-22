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
  private final SelenideElement errorText = $("p[class='form__error']");

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage goToRegisterPage() {
    createAccountButton.click();
    return new RegisterPage();
  }

  public LoginPage checkThatPageLoaded() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(visible);
    createAccountButton.shouldBe(visible);
    return this;
  }

  public LoginPage checkErrorAfterSubmitWithBadCredentials() {
    submitButton.click();
    errorText.shouldHave(text("Неверные учетные данные пользователя"));
    return this;
  }
}
