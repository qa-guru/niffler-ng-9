package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Friends table should be empty for new user")
    void friendsTableShouldBeEmptyForNewUser(@UserType() StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPageFromHeader()
                .checkThatPageOpen()
                .checkThatFriendsTableIsEmpty();
    }

    @Test
    @DisplayName("Friend should be present in friends table")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPageFromHeader()
                .checkThatPageOpen()
                .findFriend(user.friend());
    }

    @Test
    @DisplayName("Income invitation should be present in friends table")
    void incomeInvitationShouldBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPageFromHeader()
                .checkThatPageOpen()
                .checkFriendshipRequest(user.income());
    }

    @Test
    @DisplayName("Outcome invitation should be present in friends table")
    void outcomeInvitationShouldBePresentInAllPeopleTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openAllPeoplePageFromHeader()
                .checkThatPageOpen()
                .findPeopleWithSearchInput(user.outcome());
    }

}
