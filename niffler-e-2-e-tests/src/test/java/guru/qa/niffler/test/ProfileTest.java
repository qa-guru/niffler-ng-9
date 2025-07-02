package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class ProfileTest {
    ProfilePage profilePage = new ProfilePage();
    LoginPage loginPage = new LoginPage();
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category){
        loginPage.openPage()
                .fillLoginPage(category.username(), CFG.defaultPassword())
                .submit();
        profilePage.openPage()
                .verifyOpened();
        Assert.isTrue(profilePage.isCategoryShown(category.name()), "Archived category shown");

    }

    @Category(
            username = "duck",
            archived = false)
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category){
        loginPage.openPage()
                .fillLoginPage(category.username(), CFG.defaultPassword())
                .submit();
        profilePage.openPage()
                .verifyOpened()
                .isCategoryShown(category.name());
    }
}
