package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    FriendsPage friendsPage = new FriendsPage();
    PeoplePage peoplePage = new PeoplePage();
    LoginPage loginPage = new LoginPage();

    @Test
    @User(
            friends = 1
    )
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final UserJson friend = user.testData().friends().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .friendsPage()
                .checkExistingFriends(friend.username());
    }


    @Test
    @User()
    void friendsTableShouldBeEmptyForNewUser(UserJson user){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), "12345")
                .checkThatPageLoaded()
                .friendsPage()
                .checkNoExistingFriends();
    }

    @Test
    @User(
            incomeInvitations = 1
    )
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        final UserJson income = user.testData().incomeInvitations().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .friendsPage()
                .checkExistingInvitations(income.username());
    }


    @Test
    @User(
            outcomeInvitations = 1
    )
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        final UserJson outcome = user.testData().outcomeInvitations().getFirst();
        loginPage.openPage()
                .fillLoginPage(user.username(), "12345")
                .submit();
        peoplePage.openPage()
                .checkUserInWaitingStatus(outcome.username());
    }

}
