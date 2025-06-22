package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signInTransferButton = $(".form_sign-in");
    private final SelenideElement errorMessage = $(byText("Passwords should be equal"));

    public RegisterPage submitButtonClick() {
        submitButton.click();
        return this;
    }

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String username) {
        passwordInput.setValue(username);
        return this;
    }

    public RegisterPage setSubmitPassword(String username) {
        passwordSubmitInput.setValue(username);
        return this;
    }

    public RegisterPage fillRegistrationPageWithCorrectSubmitPass(String username, String password) {
        setUsername(username);
        setPassword(password);
        setSubmitPassword(password);
        return this;
    }

    public RegisterPage fillRegistrationPageWithUncorrectSubmitPass(String username, String password) {
        setUsername(username);
        setPassword(password);
        setSubmitPassword(password + "qwe");
        return this;
    }

    public LoginPage submitRegistration() {
        submitButton.click();
        signInTransferButton.click();
        return new LoginPage();
    }

    public RegisterPage checkPasswordShouldBeEqualErrorMessage() {
        errorMessage.should(visible);
        errorMessage.getText().equals(ErrorMessages.PASSWORDS_SHOULD_BE_EQUAL.getText());
        return this;
    }
}
