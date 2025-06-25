package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmit = $("#passwordSubmit"),
            submitButton = $("#register-button"),
            signInButton = $(".form_sign-in"),
            congratulationMessage = $(".form__paragraph_success"),
            errorMessage = $(".form__error");

    public RegisterPage open() {
        Selenide.open("http://127.0.0.1:9000/register");
        return this;
    }

    public RegisterPage setUserName(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmit.setValue(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage signInRegistration() {
        signInButton.click();
        return new LoginPage();
    }

    public void checkSuccessfulRegistration(String congratulation) {
        congratulationMessage.shouldHave(text(congratulation));
    }

    public void checkUnsuccessfulRegistrationWithExistUserName(String text) {
        errorMessage.shouldHave(text(text));
    }
}