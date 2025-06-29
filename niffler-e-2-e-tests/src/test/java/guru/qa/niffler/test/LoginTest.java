package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class LoginTest {

    LoginPage loginPage = new LoginPage();
    private static final Config CFG = Config.getInstance();

    @Test
    @Description("Проверяем успешную авторизацию")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .checkThatPageLoaded()
                .checkThatTitleStaticsIsVisible()
                .checkThatTitleHistoryIsVisible();
    }

    @Test
    @Description("Проверяем, что после ввода некорректных данных авторизация не происходит")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("87897", "780870")
                .submit();

        loginPage.titleLogInIsVisible().badCredentialsErrorIsVisible();
    }
}
