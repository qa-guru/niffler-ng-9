package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement registerButton = $("button[type='submit']");
    private final SelenideElement successRegisterText = $(byText("Congratulations! You've registered!"));
    private final SelenideElement loginButton = $(byText("Sign in"));
    private final SelenideElement formError = $(".form__error");

    private final String USERNAME_EXIST = "already exists";
    private final String PASSWORDS_EQUAL = "Passwords should be equal";

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegistration() {
        registerButton.click();
        return new RegisterPage();
    }

    public void checkSuccessfullRegistration() {
        successRegisterText.should(visible);
        loginButton.should(visible);
    }

    public void checkUsernameAlreadyExistError() {
        formError.shouldHave(text(USERNAME_EXIST));
    }

    public void checkPasswordsShouldBeEqualError() {
        formError.shouldHave(exactText(PASSWORDS_EQUAL));
    }
}
