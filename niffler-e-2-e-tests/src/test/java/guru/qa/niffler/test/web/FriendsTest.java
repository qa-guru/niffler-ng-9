package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;
import static guru.qa.niffler.page.Pages.*;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsTest {

  private static final Config CFG = Config.getInstance();

  @Test
  public void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
    Selenide.open(CFG.frontUrl());
    String username = user.username();
    loginPage.fillLoginPage(username, user.password())
            .submit()
            .checkThatPageLoaded();
    mainPage.friendsPage()
            .checkFriendInTable(user.friend());
  }

  @Test
  public void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
    Selenide.open(CFG.frontUrl());
    String username = user.username();
    loginPage.fillLoginPage(username, user.password())
            .submit()
            .checkThatPageLoaded();
    mainPage.friendsPage()
            .checkEmptyTable();
  }

  @Test
  public void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
    Selenide.open(CFG.frontUrl());
    String username = user.username();
    loginPage.fillLoginPage(username, user.password())
            .submit()
            .checkThatPageLoaded();
    mainPage.friendsPage()
            .checkIncomeFriendRequest(user.income());
  }

  @Test
  public void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
    Selenide.open(CFG.frontUrl());
    String username = user.username();
    loginPage.fillLoginPage(username, user.password())
            .submit()
            .checkThatPageLoaded();
    mainPage.friendsPage()
            .checkOutcomeFriendRequest(user.outcome());
  }
}
