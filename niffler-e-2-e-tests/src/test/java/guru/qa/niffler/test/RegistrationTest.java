package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RegistrationTest {

    @DisplayName("Тест на успешную регистрацию нового пользователя")
    @Test
    void shouldRegisterNewUse(){

        String username = new Faker().name().username();
        String password = String.valueOf(10000 + new Random().nextInt(90000));
        String successMessage ="Congratulations! You've registered!";

        new RegisterPage()
                .open()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessfulRegistration(successMessage);
    }
}
