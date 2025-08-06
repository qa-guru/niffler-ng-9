package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final SelenideElement peopleTabs = $("div[aria-label='People tabs']");
    private final SelenideElement noFriendsTextMessage = $("p.MuiTypography-h6");
    private final SelenideElement myFriendsTitle = $("#simple-tabpanel-friends h2.MuiTypography-h5");
    private final SelenideElement friendsTable = $("table.MuiTable-root");
    private final SelenideElement friendNameText = $("p.MuiTypography-body1");
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final ElementsCollection friendTableRow = $$("tr.MuiTableRow-root");
    private final ElementsCollection buttons = $$("button");

    private final String TEXT_NO_USERS = "There are no users yet";
    private final String TITLE_MY_FRIENDS = "My friends";
    private final String TITLE_FRIEND_REQUESTS = "Friend requests";
    private final String BUTTON_UNFRIEND = "Unfriend";
    private final String TEXT_WAITING = "Waiting...";
    private final String BUTTON_ACCEPT = "Accept";
    private final String BUTTON_DECLINE = "Decline";


    public FriendsPage checkThatPageLoaded() {
        peopleTabs.should(visible);
        return this;
    }

    public void checkMessageNoUsers() {
        noFriendsTextMessage.should(visible)
                .shouldHave(text(TEXT_NO_USERS));
    }

    public FriendsPage checkListOfFriends() {
        myFriendsTitle.should(visible)
                .shouldHave(text(TITLE_MY_FRIENDS));
        friendsTable.should(visible);
        return this;
    }

    public void checkFriendInFriendsTable(String friendName) {
        friendNameText.should(visible)
                .shouldHave(text(friendName));
        buttons.findBy(text(BUTTON_UNFRIEND)).should(visible);
    }

    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.should(visible).click();
        return new FriendsPage();
    }

    public void checkOutcomeInvitationBePresent(String username) {
        friendTableRow.findBy(text(username))
                .$("span.MuiChip-label").shouldHave(text(TEXT_WAITING));
    }

    public FriendsPage checkTitleFriendRequests() {
        myFriendsTitle.should(visible)
                .shouldHave(text(TITLE_FRIEND_REQUESTS));
        return this;
    }

    public void checkIncomeInvitationBePresent(String username) {
        friendTableRow.findBy(text(username))
                .$("p.MuiTypography-body1").shouldHave(text(username));
        buttons.findBy(text(BUTTON_ACCEPT)).should(visible);
        buttons.findBy(text(BUTTON_DECLINE)).should(visible);
    }
}
