package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {

    private final SelenideElement userNameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement singUpButton = $("#register-button");
    private final SelenideElement congratulationsText = $x("//p[contains(text(), 'Congratulations!')]");
    private final SelenideElement errorMessage = $("span[class = 'form__error']");

    public RegisterPage setUserName(String userName) {
        userNameInput.setValue(userName);
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
        singUpButton.click();
        return this;
    }

    public void checkCongratulationsIsVisible() {
        congratulationsText.shouldBe(visible);
    }

    public void checkErrorMessageUserNameAlreadyExists(String userName) {
        errorMessage.shouldHave(text("Username `" + userName + "` already exists"));
    }

    public void checkErrorMessagePasswordAndConfirmPasswordAreNotEqual() {
        errorMessage.shouldHave(text("Passwords should be equal"));
    }
}
