package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    private static final Config CFG = Config.getInstance();

    static Faker faker = new Faker();

    private static String userName;
    private static String password;

    @BeforeAll
    static void getRandomUserNameAndPassword(){
        userName = faker.name().firstName();
        password = faker.regexify("[A-Za-z0-9]{6}");
    }

    @Test
    @Description("Проверяем успешную регистрацию")
    void shouldRegisterNewUser() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegistration()
                .setUserName(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkCongratulationsIsVisible();
    }

    @Test
    @Description("Проверяем, что нельзя зарегистрироваться в системе с уже существующим именем пользователя")
    void shouldNotRegisterUserWithExistingUserName() {
        String existUserName = "duck";
        String existPassword = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegistration()
                .setUserName(existUserName)
                .setPassword(existPassword)
                .setPasswordSubmit(existPassword)
                .submitRegistration()
                .checkErrorMessageUserNameAlreadyExists(existUserName);
    }

    @Test
    @Description("Проверяем, что регистрация не произошла при не совпадающих паролях")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegistration()
                .setUserName(userName)
                .setPassword(password)
                .setPasswordSubmit(password + "1")
                .submitRegistration()
                .checkErrorMessagePasswordAndConfirmPasswordAreNotEqual();
    }
}
