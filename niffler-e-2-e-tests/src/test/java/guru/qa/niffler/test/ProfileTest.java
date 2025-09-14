package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
    ProfilePage profilePage = new ProfilePage();
    LoginPage loginPage = new LoginPage();
    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            categories = {@Category(
                archived = true
            )}
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson[] categories){
        CategoryJson category = categories[0];
        loginPage.openPage()
                .fillLoginPage(category.username(), CFG.defaultPassword())
                .submit();
        profilePage.openPage()
                .verifyOpened()
                .checkCategoryNotShown(category.name());

    }

    @User(
            username = "duck",
            categories = {@Category(
                    archived = false
            )}
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category){
        loginPage.openPage()
                .fillLoginPage(category.username(), CFG.defaultPassword())
                .submit();
        profilePage.openPage()
                .verifyOpened()
                .checkCategoryShown(category.name());
    }
}
