package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class ToolBar {
    private final SelenideElement newSpendingButton = $("header .MuiButton-contained");
    private final SelenideElement nifflerLink = $("header .MuiBox-root a");
    private final SelenideElement menuButton = $("header button.MuiButtonBase-root");
    private final SelenideElement profileLink = $("[href='/profile']");
    private final SelenideElement friendsLink = $("[href='/people/friends']");
    private final SelenideElement allPeopleLink = $("[href='/people/all']");
    private final SelenideElement singOutLink = $("");

    public NewSpendingPage clickSpendingButton() {
        newSpendingButton.shouldBe(enabled).click();
        return page(NewSpendingPage.class);
    }

    public MainPage clickNifflerLink() {
        nifflerLink.shouldBe(enabled).click();
        return page(MainPage.class);
    }

    public ToolBar clickMenuButton() {
        menuButton.shouldBe(enabled).click();
        return this;
    }

    public ToolBar clickMenuVisible() {
        profileLink.shouldBe(visible, exactText("Profile"));
        friendsLink.shouldBe(visible, exactText("Friends"));
        allPeopleLink.shouldBe(visible, exactText("All People"));
        return this;
    }

    public ProfilePage clickProfileLink() {
        profileLink.shouldBe(enabled).click();
        return page(ProfilePage.class);
    }

    public FriendsPage clickFriendsLink() {
        friendsLink.shouldBe(enabled).click();
        return new FriendsPage();
    }
}
