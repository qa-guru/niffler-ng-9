package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

public class FriendsTest {
    FriendsPage friendsPage = new FriendsPage();
    PeoplePage peoplePage = new PeoplePage();
    LoginPage loginPage = new LoginPage();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UsersQueueExtension.UserType(WITH_FRIEND) StaticUser user) {
        loginPage.openPage()
                .fillLoginPage(user.username(), user.password())
                .submit();
        friendsPage.openPage()
                .checkFriendsContainUsers(List.of(user.friend()));
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UsersQueueExtension.UserType(EMPTY) StaticUser user){
        loginPage.openPage()
                .fillLoginPage(user.username(), user.password())
                .submit();
        friendsPage.openPage()
                .checkFriendsEmpty();
        peoplePage.openPage()
                .checkNoOutcomeInvites();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        loginPage.openPage()
                .fillLoginPage(user.username(), user.password())
                .submit();
        friendsPage.openPage()
                .checkRequestsContainUsers(List.of(user.income()));
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        loginPage.openPage()
                .fillLoginPage(user.username(), user.password())
                .submit();
        peoplePage.openPage()
                .checkUserInWaitingStatus(user.outcome());
    }

    @AfterAll
    static void suiteTearDown() {
        Selenide.closeWebDriver();
    }
}
