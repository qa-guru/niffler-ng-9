package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RegisterTest {
    private static final Config CFG = Config.getInstance();
    private final String randomUsername = "Nick_" + System.currentTimeMillis();
    private final String randomPassword = "Pass" + new Random().nextInt(10000);


    @Test
    void userGetErrorMessageWithWrongSubmitPass() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick()
                .fillRegistrationPageWithUncorrectSubmitPass(randomUsername, randomPassword)
                .submitButtonClick()
                .checkPasswordShouldBeEqualErrorMessage();
    }

    @Test
    void loginPageShouldBeOpenedAfterRegistration() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick()
                .fillRegistrationPageWithCorrectSubmitPass(randomUsername, randomPassword)
                .submitRegistration()
                .checkThatPageLoaded();
    }

    @Test
    void mainPageShouldBeOpenedAfterRegistrationAndLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick()
                .fillRegistrationPageWithCorrectSubmitPass(randomUsername, randomPassword)
                .submitRegistration()
                .checkThatPageLoaded()
                .fillLoginPage(randomUsername, randomPassword)
                .submit()
                .checkThatPageLoaded();
    }
}
