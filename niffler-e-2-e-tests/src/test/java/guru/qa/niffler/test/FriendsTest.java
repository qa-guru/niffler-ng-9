package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsTest {

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        login(user.username(), user.password());
        FriendsPage.open()
                .checkThatFriendPresent(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        login(user.username(), user.password());
        FriendsPage.open()
                .checkThatFriendsDoNotExist();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        login(user.username(), user.password());
        FriendsPage.open()
                .checkThatFriendPresent(user.income_requests());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        login(user.username(), user.password());
        PeoplePage.open()
                .checkOutcomeFriendRequest(user.outcome_requests());
    }

    private void login(String username, String password) {
        LoginPage.open()
                .fillLoginPage(username, password)
                .submit()
                .checkThatPageLoaded();
    }
}
