package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();

    String username = faker.name().username();

    @Test
    @DisplayName("User should be registered with valid username and password")
    void userWithUniqueUsernameAndValidPasswordShouldBeRegistered(){
        final String password = "12345";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    @DisplayName("User should be registered with valid uppercase username and password")
    void userWithUpperCaseUsernameShouldBeRegistered(){
        final String password = "12345";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username.toUpperCase(), password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    @DisplayName("User should be registered with valid username which contains digits and password")
    void userUsernameContainsDigitsShouldBeRegistered(){
        final String password = "12345";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username + "1", password, password)
                .submitRegistration()
                .checkSucceedRegistrationPageTitle();
    }

    @Test
    @DisplayName("User can open login page and login to account after registration")
    void userCanMakeLoginAfterRegistration(){
        final String password = "12345";

        open(CFG.frontUrl(), LoginPage.class)
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
    @DisplayName("User with taken username should be not registered")
    @Disabled
    void userShouldBeNotRegisteredIfUsernameAlreadyTaken(){

    }

    @Test
    @DisplayName("User with short username should be not registered")
    void userShouldBeNotRegisteredIfUsernameIsShort(){
        final String password = "12345";
        final String errorText = "Allowed username length should be from 3 to 50 characters";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm("us", password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    @DisplayName("User with username more than 50 characters should be not registered")
    void userShouldBeNotRegisteredIfUsernameIsLong(){
        final String username = faker.lorem().sentence(20)
                .replaceAll("\\s", "");
        final String password = "12345";
        final String errorText = "Allowed username length should be from 3 to 50 characters";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    @DisplayName("User with blank username should be not registered")
    void userShouldBeNotRegisteredIfUsernameIsBlank(){
        final String password = "12345";
        final String errorText = "Username can not be blank";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm("     ", password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    @DisplayName("User with username contains whitespaces should be not registered")
    void userShouldBeNotRegisteredIfUsernameContainsWhitespaces(){
        final String username = "test username";
        final String password = "12345";
        final String errorText = "Username must not contain whitespaces";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckUsernameError(errorText);
    }

    @Test
    @DisplayName("Error should be present if passwords don't match")
    void errorShouldBeShownWhenPasswordsDontMatch(){
        final String password = "12345";
        final String errorText = "Passwords should be equal";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password + "1")
                .submitRegistrationAndCheckPasswordError(errorText);
    }

    @Test
    @DisplayName("Error should be present if password shorter than 3 characters")
    void errorShouldBeShownWhenPasswordTooShort(){
        final String password = "12";
        final String errorText = "Allowed password length should be from 3 to 12 characters";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckPasswordError(errorText);
    }

    @Test
    @DisplayName("Error should be present if password longer than 12 characters")
    void errorShouldBeShownWhenPasswordTooLong(){
        final String password = "1234567890123";
        final String errorText = "Allowed password length should be from 3 to 12 characters";

        open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationForm()
                .fillRegistrationForm(username, password, password)
                .submitRegistrationAndCheckPasswordError(errorText);
    }
}
