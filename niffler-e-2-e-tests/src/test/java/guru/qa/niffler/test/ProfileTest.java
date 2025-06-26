package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "test1",
            archived = true
    )
    @DisabledByIssue("3")
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
            username = "test2",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson, @UserType(isEmpty = true) StaticUser user){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .checkCategory(categoryJson.name());
    }

}
