package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {
    private final SelenideElement alreadyHaveAnAccountLoginLink= $("a[href='/login']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordButton = $("#passwordBtn");
    private final SelenideElement confirmPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement confirmPasswordButton = $("#passwordSubmitBtn");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successRegistrationText = $(byText("Congratulations! You've registered!"));
    private final SelenideElement loginButton = $x("//a[text()='Sign in']");
    private final SelenideElement errorText = $("span[class='form__error']");

    public LoginPage clickOnAlreadyHaveAnAccountLoginLink() {
        alreadyHaveAnAccountLoginLink.click();
        return new LoginPage();
    }

    public RegisterPage checkThatPageLoaded() {
        loginButton.shouldBe(visible);
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordButton.shouldBe(visible);
        confirmPasswordInput.shouldBe(visible);
        confirmPasswordButton.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    public RegisterPage fillRegisterPage(String username, String password, String confirmPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(confirmPassword);
        return this;
    }

    public RegisterPage revealPassword() {
        passwordButton.click();
        return this;
    }

    public RegisterPage revealConfirmPassword() {
        confirmPasswordButton.click();
        return this;
    }

    public RegisterPage checkThatPasswordIsHidden() {
        passwordInput.shouldHave(attribute("type", "password"));
        return this;
    }

    public RegisterPage checkThatPasswordIsRevealed() {
        passwordInput.shouldHave(attribute("type", "text"));
        return this;
    }

    public RegisterPage checkThatConfirmPasswordIsHidden() {
        confirmPasswordInput.shouldHave(attribute("type", "password"));
        return this;
    }

    public RegisterPage checkThatConfirmPasswordIsRevealed() {
        confirmPasswordInput.shouldHave(attribute("type", "text"));
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public RegisterPage checkThatRegistrationIsSuccessful() {
        successRegistrationText.shouldBe(visible);
        return this;
    }

    public LoginPage goToLoginPageAfterRegistration() {
        loginButton.click();
        return new LoginPage();
    }

    public RegisterPage checkErrorText(String errorText) {
        this.errorText.shouldBe(text(errorText));
        return this;
    }
}
