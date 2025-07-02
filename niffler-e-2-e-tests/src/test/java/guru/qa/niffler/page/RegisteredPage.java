package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.Pages.loginPage;

public class RegisteredPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");

    public RegisteredPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisteredPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisteredPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    public RegisteredPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage checkSuccessRegistered() {
        $x("//p[text() = \"Congratulations! You've registered!\"]").shouldBe(visible);
        return loginPage;
    }

    public RegisteredPage checkFailedUserRegistered(String username) {
        $x("//span[text() = 'Username `" + username + "` already exists']").shouldBe(visible);
        return this;
    }

    public RegisteredPage checkFailedPasswordRegistered() {
        $x("//span[text() = 'Passwords should be equal']").shouldBe(visible);
        return this;
    }
}