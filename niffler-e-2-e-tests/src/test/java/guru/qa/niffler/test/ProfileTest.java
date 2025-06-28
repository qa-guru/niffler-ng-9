package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

public class ProfileTest {
    private final Config CFG = Config.getInstance();

    @Category(
            username = "testUser3",
            archived = true
    )

    @DisplayName("Архивная категория должна присутствовать в списке категорий")
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("testUser3", "34567")
                .submit()
                .checkThatPageLoaded()
                .navigateToProfilePage()
                .showArchivedCategories()
                .checkCategoryInCategoryList(categoryJson.name());
    }

    @Category(
            username = "testUser3",
            archived = false
    )
    @DisplayName("Активная категория должна присутствовать в списке категорий")
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("testUser3", "34567")
                .submit()
                .checkThatPageLoaded()
                .navigateToProfilePage()
                .checkCategoryInCategoryList(category.name());
    }
}
