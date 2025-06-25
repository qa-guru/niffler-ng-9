package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private static final Config CFG = Config.getInstance();

    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmit = $("#passwordSubmit"),
            submitButton = $("#register-button"),
            congratulationMessage = $(".form__paragraph_success"),
            errorMessage = $(".form__error");

    public void open() {
        Selenide.open(CFG.registerPageUrl());
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

    public void checkSuccessfulRegistration(String congratulation) {
        congratulationMessage.shouldHave(text(congratulation));
    }

    public void checkUnsuccessfulRegistrationWithExistUserName(String text) {
        errorMessage.shouldHave(text(text));
    }

    public void checkUnsuccessfulRegistrationIfPasswordAndConfirmPasswordAreNotEqual(String text) {
        errorMessage.shouldBe(text(text));
    }
}