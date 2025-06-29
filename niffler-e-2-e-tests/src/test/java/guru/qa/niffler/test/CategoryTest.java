package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(BrowserExtension.class)
public class CategoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryTest.class);
    private static final Config CONFIG = Config.getInstance();
    private static final String ADMIN_ACCOUNT = "duck";
    private static final String ADMIN_PWD = "12345";

    @Category(
            username = "duck"
    )
    @Test
    void activeCategoryShouldBeArchived(CategoryJson json) {
        String categoryName = json.name();
        ProfilePage profilePage = new ProfilePage();
        Selenide.open(CONFIG.frontUrl());
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
        profilePage.profileButtonClick()
                .checkMenuListIsPresent()
                .profileLinkClick()
                .checkProfileHeader()
                .archiveCategory(categoryName)
                .showArchivedCategories()
                .checkArchiveCategory(categoryName);
    }

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldBeUnarchived(CategoryJson json) {
        String categoryName = json.name();
        ProfilePage profilePage = new ProfilePage();
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
        profilePage.profileButtonClick()
                .checkMenuListIsPresent()
                .profileLinkClick()
                .checkProfileHeader()
                .showArchivedCategories()
                .unarchiveCategory(categoryName)
                .checkUnarchiveCategory(categoryName);
    }
}
