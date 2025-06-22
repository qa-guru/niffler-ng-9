package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class UserProfileTest {

    private static final Config CFG = Config.getInstance();

    private static final String username = "duck";
    private static final String password = "12345";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(username, password)
                .submit()
                .goToUserProfilePage()
                .checkThatPageLoaded();
    }

    @Category(
            username = username,
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(username, password)
                .submit()
                .goToUserProfilePage()
                .checkThatCategoryNonExist(category.name())
                .clickOnShowArchivedCheckbox(true)
                .checkThatCategoryExist(category.name());
    }

    @Category(
            username = username,
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(username, password)
                .submit()
                .goToUserProfilePage()
                .checkThatCategoryExist(category.name());
    }
}
