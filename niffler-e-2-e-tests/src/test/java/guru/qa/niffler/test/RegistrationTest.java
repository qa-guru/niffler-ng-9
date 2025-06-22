package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();

    String username = faker.name().username();

    @Test
    void userWithUniqueUsernameAndValidPasswordShouldBeRegistered(){
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    void userWithUpperCaseUsernameShouldBeRegistered(){
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username.toUpperCase(), password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    void userUsernameContainsDigitsShouldBeRegistered(){
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username + "1", password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    void userCanMakeLoginAfterRegistration(){
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle()
                .loginAfterRegistration()
                .fillLoginPage(username, password)
                .submit()
                .checkThatPageLoaded();
    }

    //TODO registration of user before test
    @Test
    void userShouldBeNotRegisteredIfUsernameAlreadyTaken(){

    }

    @Test
    void userShouldBeNotRegisteredIfUsernameIsShort(){
        final String password = "12345";
        final String errorText = "Allowed username length should be from 3 to 50 characters";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm("us", password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    void userShouldBeNotRegisteredIfUsernameIsLong(){
        final String username = faker.lorem().sentence(20)
                .replaceAll("\\s", "");
        final String password = "12345";
        final String errorText = "Allowed username length should be from 3 to 50 characters";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    void userShouldBeNotRegisteredIfUsernameIsBlank(){
        final String password = "12345";
        final String errorText = "Username can not be blank";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm("     ", password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    void userShouldBeNotRegisteredIfUsernameContainsWhitespaces(){
        final String username = "test username";
        final String password = "12345";
        final String errorText = "Username must not contain whitespaces";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    void errorShouldBeShownWhenPasswordsDontMatch(){
        final String password = "12345";
        final String errorText = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password + "1")
                .submitRegistrationAndCheckPasswordError(errorText);
    }

    @Test
    void errorShouldBeShownWhenPasswordTooShort(){
        final String password = "12";
        final String errorText = "Allowed password length should be from 3 to 12 characters";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckPasswordError(errorText);
    }

    @Test
    void errorShouldBeShownWhenPasswordTooLong(){
        final String password = "1234567890123";
        final String errorText = "Allowed password length should be from 3 to 12 characters";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckPasswordError(errorText);
    }
}
