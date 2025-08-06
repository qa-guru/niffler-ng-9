package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .checkThatPageLoaded()
                .clickFriendsButton()
                .checkThatPageLoaded()
                .checkListOfFriends()
                .checkFriendInFriendsTable(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType() StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .checkThatPageLoaded()
                .clickFriendsButton()
                .checkThatPageLoaded()
                .checkMessageNoUsers();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .checkThatPageLoaded()
                .clickFriendsButton()
                .checkThatPageLoaded()
                .checkTitleFriendRequests()
                .checkIncomeInvitationBePresent(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInFriendsTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .checkThatPageLoaded()
                .clickFriendsButton()
                .checkThatPageLoaded()
                .clickAllPeopleTab()
                .checkOutcomeInvitationBePresent(user.outcome());
    }
}