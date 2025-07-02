package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.page.Pages.*;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    Faker faker = new Faker();

    String username;
    String password;

    @BeforeEach
    public void setup() {
        username = faker.name().username();
        password = faker.number().digits(6);
    }

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl());
        loginPage.createNewUserButton();
        registeredPage
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegistered();
    }

    @Test
    void shouldNotRegisterUserWithExistingUserName() {
        Selenide.open(CFG.frontUrl());
        loginPage.createNewUserButton();
        registeredPage
                .setUsername("marina")
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkFailedUserRegistered("marina");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl());
        loginPage.createNewUserButton();
        registeredPage
                .setUsername("marina")
                .setPassword(password)
                .setPasswordSubmit("12345")
                .submitRegistration()
                .checkFailedPasswordRegistered();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl());
        loginPage
                .fillLoginPage("marina", "052322")
                .submit();
        loginPage.checkSuccessLogin();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl());
        loginPage
                .fillLoginPage("marina", "12345")
                .submit();
        loginPage.checkFailedLogin();
    }
}