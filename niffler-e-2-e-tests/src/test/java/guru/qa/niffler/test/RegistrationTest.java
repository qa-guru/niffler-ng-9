package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private String username;
    private String password;

    @BeforeEach
    public void setup() {
        username = RandomStringUtils.randomAlphanumeric(10);
        password = RandomStringUtils.randomAlphanumeric(10);
    }

    @Test
    void shouldOpenLoginPageAfterClickOn() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .clickOnAlreadyHaveAnAccountLoginLink()
                .checkThatPageLoaded();
    }

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .checkThatPageLoaded()
                .fillRegisterPage(username, password, password)
                .submitRegistration()
                .checkThatRegistrationIsSuccessful()
                .goToLoginPageAfterRegistration()
                .checkThatPageLoaded()
                .fillLoginPage(username, password)
                .submit()
                .checkThatPageLoaded();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .fillRegisterPage(username, password, password)
                .submitRegistration()
                .goToLoginPageAfterRegistration()
                .goToRegisterPage()
                .fillRegisterPage(username, password, password)
                .submitRegistration()
                .checkErrorText(String.format("Username `%s` already exists", username));
    }

    @Test
    void passwordFieldShouldBeRevealedAfterRevealButtonsClicked() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .fillRegisterPage(username, password, password)
                .checkThatPasswordIsHidden()
                .revealPassword()
                .checkThatPasswordIsRevealed()
                .checkThatConfirmPasswordIsHidden()
                .revealConfirmPassword()
                .checkThatConfirmPasswordIsRevealed();
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .fillRegisterPage(username, password, password + "_invalid")
                .submitRegistration()
                .checkErrorText("Passwords should be equal");
    }
}
