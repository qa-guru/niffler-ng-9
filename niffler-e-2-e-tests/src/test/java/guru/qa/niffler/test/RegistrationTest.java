package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationTest {
    RegisterPage registerPage = new RegisterPage();
    String password = "123456";
    private static final Config CFG = Config.getInstance();
    private final String registerPageUrl = "register";

    @Test
    void shouldRegisterNewUser() {
        String userName = randomUsername();
        registerPage.openPage()
                .registerNewUser(userName, password, password)
                .checkRegistrationSuccessful();
    }

    @Test
    void shouldNotRegisterUserWithExistingName() {
        String userName = randomUsername();
        registerPage.openPage()
                .registerNewUser(userName, password, password)
                .checkRegistrationSuccessful();
        registerPage.openPage()
                .registerNewUser(userName, password, password)
                .checkErrorUserRegisteredShown(userName);

    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String userName = randomUsername();
        registerPage.openPage()
                .registerNewUser(userName, password, password + "1")
                .checkErrorPasswordsDontCoincideShown();
    }

    @AfterAll
    static void suiteTearDown() {
        Selenide.closeWebDriver();
    }
}
