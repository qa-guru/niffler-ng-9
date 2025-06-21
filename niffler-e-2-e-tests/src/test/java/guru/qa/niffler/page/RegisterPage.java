package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
	private final SelenideElement usernameInput = $("#username");
	private final SelenideElement passwordInput = $("#password");
	private final SelenideElement submitPasswordInput = $("#passwordSubmit");
	private final SelenideElement signUpButton = $("#register-button");
	private final SelenideElement signInButton = $("a.form_sign-in");
	private final SelenideElement errorMessage = $("span.form__error");

	public RegisterPage fillUserName(String username) {
		usernameInput.setValue(username);
		return this;
	}

	public RegisterPage fillPassword(String password) {
		passwordInput.setValue(password);
		return this;
	}

	public RegisterPage submitPassword(String password) {
		submitPasswordInput.setValue(password);
		return this;
	}

	public RegisterPage fillRegisterPage(String username, String password) {
		fillUserName(username).fillPassword(password).submitPassword(password);
		return this;
	}

	public RegisterPage sighUp() {
		signUpButton.click();
		return this;
	}

	public LoginPage signUpThenSignIn() {
		signUpButton.click();
		signInButton.click();
		return new LoginPage();
	}

	public void checkErrorUserAlreadyExist(String userName) {
		String errorMessage = this.errorMessage.should(visible).text();
		Assertions.assertEquals("Username `" + userName + "` already exists", errorMessage);
	}

	public void checkErrorPasswordsShouldBeEqual() {
		String errorMessage = this.errorMessage.should(visible).text();
		Assertions.assertEquals("Passwords should be equal", errorMessage);
	}
}
