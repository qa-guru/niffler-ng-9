package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {
    private final SelenideElement
        usernameInput = $("#username"),
        passwordInput = $("#password"),
        passwordSubmitInput = $("#passwordSubmit"),
        registrationForm = $("#register-form"),
        signupBtn = $("#register-button");

    public RegistrationPage fillRegistrationForm(String username, String password, String submittedPassword){
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(submittedPassword);
        return this;
    }

    public SucceedRegistrationPage submitRegistration(){
        signupBtn.click();
        return new SucceedRegistrationPage();
    }

    public RegistrationPage submitRegistrationAndCheckUsernameError(String error){
        signupBtn.click();
        registrationForm.$$("label").find(text("Username"))
                .$("span").shouldHave(text(error));
        return this;
    }

    public RegistrationPage submitRegistrationAndCheckPasswordError(String error){
        signupBtn.click();
        registrationForm.$$("label").find(text("Password"))
                .$("span").shouldHave(text(error));
        return this;
    }
}
