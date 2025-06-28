package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

  private static final Config CFG = Config.getInstance();
  private static final String login = "duck";
  private static final String pass = "12345";

  @User(
          username = login,
          categories = @Category(
                  archived = true
          )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(login, pass)
        .checkThatPageLoaded();

    Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
        .checkArchivedCategoryExists(category.name());
  }

  @User(
          username = login,
          categories = @Category(
                  archived = false
          )
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(login, pass)
        .checkThatPageLoaded();

    Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
        .checkCategoryExists(category.name());
  }
}
