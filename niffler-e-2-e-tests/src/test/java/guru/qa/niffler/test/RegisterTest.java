package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    private static final Config CFG = Config.getInstance();
    private static final Faker faker = new Faker();

    @Test
    void registerNewUserTest() {
        User newUser = new User(faker.name().username(), faker.number().digits(5));
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(newUser.username())
                .setPassword(newUser.password())
                .setPasswordSubmit(newUser.password())
                .submit()
                .checkCongratulations();
    }
}
