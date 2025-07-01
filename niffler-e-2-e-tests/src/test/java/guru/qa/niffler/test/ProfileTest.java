package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "test1",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    @DisabledByIssue("3")
    @DisplayName("Archived category should be present in categories list on profile page")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson){
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("test1", "secret")
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .showArchivedCategories()
                .checkArchivedCategory(categoryJson.name());
    }

    @User(
            username = "test1",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    @DisplayName("Active category should be present in categories list on profile page")
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson){
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("test1", "secret")
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .checkCategory(categoryJson.name());
    }

}
