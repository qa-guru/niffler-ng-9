package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "test1",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("test1", "secret")
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .showArchivedCategories()
                .checkArchivedCategory(categoryJson.name());
    }

    @Category(
            username = "test1",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("test1", "secret")
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .checkCategory(categoryJson.name());
    }

}
