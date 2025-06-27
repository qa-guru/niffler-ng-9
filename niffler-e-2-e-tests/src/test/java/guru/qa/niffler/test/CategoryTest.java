package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.page.Pages.loginPage;
import static guru.qa.niffler.page.Pages.profilePage;

public class CategoryTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "marina",
            archived = true
    )
    @Test
    public void archivedCategoryShouldBeVisibleInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl());
        String username = "marina";
        loginPage.fillLoginPage(username, "052322")
                .submit()
                .checkThatPageLoaded();
        profilePage.openProfile(username)
                .checkArchiveCategoryIsVisible(category.name());
    }

    @Category(
            username = "marina"
    )
    @Test
    public void activeCategoryShouldBeVisibleInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl());
        String username = "marina";
        loginPage.fillLoginPage(username, "052322")
                .submit()
                .checkThatPageLoaded();
        profilePage.openProfile(username)
                .checkCategoryIsVisible(category.name());
    }
}
