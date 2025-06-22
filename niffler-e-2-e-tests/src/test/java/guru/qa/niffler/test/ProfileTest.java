package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            name = "CatyArc_",
            username = "duck",
            isArchive = false
    )
    @Test
    public void archiveCategory(){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .checkThatPageLoaded()
                .navigateToProfilePage()
                .checkThatPageLoaded()
                .checkToReduceTableRowsAfterArchiveTopCategory();
    }

    @Category(
            name = "CatUnarch_",
            username = "duck",
            isArchive = false
    )
    @Test
    public void unarchiveCategory() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .checkThatPageLoaded()
                .navigateToProfilePage()
                .checkThatPageLoaded()
                .archiveFirstCat()
                .checkToIncTableRowsAfterUnarchiveCategory();
    }
}
