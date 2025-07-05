package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {
    private final SelenideElement emptyTable = $x("//*[text() = 'There are no users yet']");
    private final SelenideElement allPeopleButton = $x("//*[text() = 'All people']");

    public FriendsPage checkFriendInTable(String friendName) {
        $x(String.format("//*[text() = '%s']", friendName)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkEmptyTable() {
        emptyTable.shouldBe(visible);
        return this;
    }

    public FriendsPage checkIncomeFriendRequest(String friendName) {
        $x(String.format(
                "//*[text() = '%s']/ancestor::td/following-sibling::*//*[text() = 'Accept']",
                friendName
        )).shouldBe(visible);
        return this;
    }

    public FriendsPage checkOutcomeFriendRequest(String friendName) {
        allPeopleButton.click();
        $$x("//*[contains(@aria-labelledby, 'tableTitle')]//*[contains(@class, 'hover')]").shouldHave(sizeGreaterThan(0));
        $x(String.format(
                "//*[text() = '%s']/ancestor::td/following-sibling::td//*[text() = 'Waiting...']",
                friendName
        )).shouldBe(visible);
        return this;
    }
}