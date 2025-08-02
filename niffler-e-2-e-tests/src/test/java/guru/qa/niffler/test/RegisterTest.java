package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {

    private static final Config CFG = Config.getInstance();
    private static final String NEW_USERNAME = "username_" + System.currentTimeMillis();
    private static final String EXISTING_USERNAME = "tinki";
    private static final String CORRECT_PASSWORD = "12345";
    private static final String WRONG_PASSWORD = "54321";


    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(NEW_USERNAME)
                .setPassword(CORRECT_PASSWORD)
                .setPasswordSubmit(CORRECT_PASSWORD)
                .submitRegistration()
                .checkSuccessfullRegistration();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(EXISTING_USERNAME)
                .setPassword(CORRECT_PASSWORD)
                .setPasswordSubmit(CORRECT_PASSWORD)
                .submitRegistration()
                .checkUsernameAlreadyExistError();
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(NEW_USERNAME)
                .setPassword(CORRECT_PASSWORD)
                .setPasswordSubmit(WRONG_PASSWORD)
                .submitRegistration()
                .checkPasswordsShouldBeEqualError();
    }
}
