package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    private final String username = "duck";
    private final String password = "12345";

    @Category(
            username = username,
            archived = true
    )
    @Test
    @Description("Проверяем, что заархивированная категория представлена в списке категорий")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(username, password)
                .submit()
                .clickToPersonalIcon()
                .goToProfile()
                .checkThatCategoryNotVisible(category.name())
                .clickOnShowArchivedSwitch()
                .checkThatCategoryVisible(category.name());
    }

    @Category(
            username = username,
            archived = false
    )
    @Test
    @Description("Проверяем, что созданная категория видна в списке категорий")
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(username, password)
                .submit()
                .clickToPersonalIcon()
                .goToProfile()
                .checkThatCategoryVisible(category.name());
    }
}

