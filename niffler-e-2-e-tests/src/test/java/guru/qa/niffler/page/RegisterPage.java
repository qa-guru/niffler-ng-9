package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement congratulationsMessage = $(".form__paragraph.form__paragraph_success");
  //'Congratulations! You've registered!'
  private final SelenideElement singInButton = $("a.form_sign-in");
  private final SelenideElement errorMessage = $(".form__error");
  //Username `max` already exists
  //'Passwords should be equal'

  @Step("Fill username {0}, password {1}, submit password {1}")
  public RegisterPage fillRegisterForm(String username,String password, String passwordSubmit){
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  @Step("Submit registration")
  public RegisterPage submitRegistration(){
    submitButton.click();
    return this;
  }

  @Step("Check congratulations message")
  public RegisterPage checkCongratulationsMessage(){
    congratulationsMessage.should(visible);
    return this;
  }

  @Step("Sing In")
  public LoginPage singIn(){
    singInButton.click();
    return new LoginPage();
  }

  @Step("Error Message: Username `{0}` already exists")
  public void checkErrorMessage(){
    //String error = String.format("Username `%s` already exists", username);
    errorMessage.should(visible);
  }

  public String getErrorMessage(){
    return errorMessage.getText();
  }

}
