package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final SelenideElement emptyTable = $x("//*[text() = 'There are no users yet']");
    private final SelenideElement allPeopleButton = $x("//*[text() = 'All people']");
    private final List<SelenideElement> allPeopleList = Collections.singletonList($x("//*[text() = 'All people']//*[contains(@aria-labelledby, 'tableTitle')]//*[contains(@class, 'hover')]"));

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