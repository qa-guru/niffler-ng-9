package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.TYPE.*;

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith({UsersQueueExtension.class})
    void friendShouldBePresentInFriendsTableTest(@UserType(value = WITH_FRIEND) StaticUser user1,
                                             @UserType(value = WITH_FRIEND) StaticUser user2) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user1.username(), user1.password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .friendShouldBeVisible(user2);
    }

    @Test
    @ExtendWith({UsersQueueExtension.class})
    void friendShouldBeEmptyInFriendsTableTest(@UserType(value = EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .friendShouldBeEmpty();
    }

    @Test
    @ExtendWith({UsersQueueExtension.class})
    void incomingRequestShouldBePresentTest(@UserType(value = WITH_INCOME_REQUEST) StaticUser userIncome,
                                            @UserType(value = WITH_OUTCOME_REQUEST) StaticUser userOutCome) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(userIncome.username(), userIncome.password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .checkIncomingRequest(userOutCome);
    }
}
