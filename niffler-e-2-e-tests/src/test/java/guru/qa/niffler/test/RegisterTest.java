package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterTest.class);
    private static final Config CONFIG = Config.getInstance();
    private static final Faker FAKER = new Faker();
    private static final String ADMIN_ACCOUNT = "duck";
    private static final String ADMIN_PWD = "12345";
    private static final String PWD_TEMPLATE = "?????";

    @Test
    void shouldRegisterNewUser() {
        String password = FAKER.letterify(PWD_TEMPLATE);
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(FAKER.name().firstName().concat(String.valueOf(LocalDateTime.now().hashCode())))
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegistration();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        StringBuilder password = new StringBuilder(FAKER.letterify(PWD_TEMPLATE));
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(ADMIN_ACCOUNT)
                .setPassword(password.toString())
                .setPasswordSubmit(password.toString())
                .submitRegistration()
                .checkUsernameAlreadyExistMessage(ADMIN_ACCOUNT);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        StringBuilder password = new StringBuilder(FAKER.letterify(PWD_TEMPLATE));
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(FAKER.name().firstName().concat(String.valueOf(LocalDateTime.now().hashCode())))
                .setPassword(password.toString())
                .setPasswordSubmit(password.reverse().toString())
                .submitRegistration()
                .checkPasswordNotEqualMessage();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        LoginPage loginPage = new LoginPage();
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_PWD, ADMIN_ACCOUNT)
                .submit();
        loginPage.checkBadCredentialsMessage();
    }
}