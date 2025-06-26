package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegisterPage implements Checked<RegisterPage>{

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmit = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final SelenideElement error = $(".form__error");
    private final SelenideElement congratulations = $(".form__paragraph_success");

    public RegisterPage setUserName(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmit.shouldBe(visible).setValue(password);
        return this;
    }

    public RegisterPage checkErrorUserName(String errorUserName) {
        error.shouldBe(visible, exactText("Username `" + errorUserName + "` already exists\n"));
        return this;
    }

    public RegisterPage checkPasswordError() {
        error.shouldBe(visible, exactText("Passwords should be equal\n"));
        return this;
    }

    public RegisterPage checkCongratulations() {
        congratulations.shouldBe(visible, exactText("Congratulations! You've registered!\n"));
        return this;
    }

    public LoginPage clickSingInButton() {
        submitButton.shouldBe(enabled).click();
        return page(LoginPage.class);
    }
    public RegisterPage submit() {
        submitButton.shouldBe(enabled).click();
        return this;
    }

    @Override
    public RegisterPage checkOpen() {
        $("h1").shouldBe(visible, exactText("Sign up"));
        return this;
    }
}
