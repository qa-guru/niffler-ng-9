package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $("#register-button");
    private final SelenideElement logInTitle = $("h1");
    private final SelenideElement badCredentialsErrorMessage = $x("//p[contains(text(), 'Неверные учетные данные')]");


    public LoginPage fillLoginPage(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        return this;
    }

    public MainPage submit() {
        submitButton.click();
        return new MainPage();
    }

    public RegisterPage goToRegistration() {
        registerButton.click();
        return new RegisterPage();
    }

    public LoginPage titleLogInIsVisible() {
        logInTitle.shouldBe(visible);
        return this;
    }

    public void badCredentialsErrorIsVisible() {
        badCredentialsErrorMessage.shouldBe(visible);
    }
}
