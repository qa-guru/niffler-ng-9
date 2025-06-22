package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("#register-button");
  private final SelenideElement badCredentialsErrorMessage = $(byText("Bad credentials"));

  public LoginPage checkThatPageLoaded() {
    registerButton.should(visible);
    return this;
  }

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }

  public LoginPage clickSubmitButton() {
    submitButton.click();
    return this;
  }

  public RegisterPage createNewAccountClick() {
    registerButton.click();
    return new RegisterPage();
  }

  public LoginPage checkBadCredentialsErrorMessage() {
    badCredentialsErrorMessage.should(visible);
    badCredentialsErrorMessage.getText().equals(ErrorMessages.BAD_CREDENTIALS.getText());
    return this;
  }
}
