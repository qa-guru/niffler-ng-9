package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement signUpButton = $("#register-button");
    private final SelenideElement registrationSuccessfulLabel =
            $x("//p[contains(text(), \"Congratulations! You've registered!\")]");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final String errorUserRegisteredLabel = "//span[text() = 'Username `%s` already exists']";
    private final SelenideElement errorPasswordsCoincideLabel = $x("//span[text() = 'Passwords should be equal']");

    public RegisterPage openPage() {
        Selenide.open(Config.getInstance().authUrl() + "register");
        return this;
    }

    public RegisterPage setUsername(String username) {
        usernameInput.shouldBe(visible).sendKeys(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.shouldBe(visible).sendKeys(password);
        return this;
    }

    public RegisterPage setSubmitPassword(String password) {
        submitPasswordInput.shouldBe(visible).sendKeys(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        signUpButton.shouldBe(clickable).click();
        return this;
    }

    public RegisterPage registerNewUser(String username, String password, String submitPassword) {
        return this.setUsername(username)
                .setPassword(password)
                .setSubmitPassword(submitPassword)
                .submitRegistration();
    }

    public RegisterPage isRegistrationSuccessful() {
        assertTrue(registrationSuccessfulLabel.isDisplayed());
        return this;
    }

    public LoginPage clickSignIn() {
        signUpButton.shouldBe(visible).click();
        return new LoginPage();
    }

    public RegisterPage isErrorUserRegisteredShown(String username) {
        SelenideElement errorMessage = $x(errorUserRegisteredLabel.formatted(username));
        errorMessage.shouldBe(visible);
        return this;
    }

    public RegisterPage isErrorPasswordsDontCoincideShown() {
        errorPasswordsCoincideLabel.shouldBe(visible);
        return this;
    }

}
