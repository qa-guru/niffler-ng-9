package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RegistrationTest {

    private final String username = new Faker().name().username();
    private final String password = String.valueOf(10000 + new Random().nextInt(90000));

    @DisplayName("Тест на успешную регистрацию нового пользователя")
    @Test
    void shouldRegisterNewUse(){

        String successMessage ="Congratulations! You've registered!";

        new RegisterPage()
                .open()
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessfulRegistration(successMessage);
    }

    @DisplayName("Тест на невозможность регистрации с существующим именем пользователя")
    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String ExistUserName = "testUser2";
        new RegisterPage()
                .open()
                .setUserName(ExistUserName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkUnsuccessfulRegistrationWithExistUserName("Username `testUser2` already exists");
    }
}
