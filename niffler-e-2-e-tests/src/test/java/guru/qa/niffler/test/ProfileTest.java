package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private static final String CORRECT_PASSWORD = "425928155";

    @Category(
            username = "vikotoed",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        ProfilePage profilePage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(categoryJson.username(), CORRECT_PASSWORD)
                .submit()
                .checkThatPageLoaded()
                .clickProfileButton()
                .checkCategoryContainerVisible();

        int initialSize = profilePage.getCategoryItemsCount();

        profilePage
                .checkArchivedSwitcherUnchecked()
                .clickShowArchivedSwitcher()
                .checkArchivedSwitcherChecked()
                .checkCategoryCountIncreased(initialSize)
                .checkArchivedCategoryVisible(categoryJson.name());
    }

    @Category(
            username = "vikotoed"
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(categoryJson.username(), CORRECT_PASSWORD)
                .submit()
                .checkThatPageLoaded()
                .clickProfileButton()
                .checkArchivedSwitcherUnchecked()
                .checkActiveCategoryVisible(categoryJson.name());
    }
}
