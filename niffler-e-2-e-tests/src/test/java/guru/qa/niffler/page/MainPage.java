package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.Pages.friendsPage;

public class MainPage {
    private final SelenideElement profileIcon = $x("//*[@data-testid='PersonIcon']//ancestor::button");
    private final SelenideElement friendsButton = $x("//*[text()='Friends']");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement spendingTable = $("#spendings");

    public FriendsPage friendsPage() {
        profileIcon.click();
        friendsButton.click();

        $x("//*[text() = 'Friends']").shouldBe(visible);
        return friendsPage;
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkThatPageLoaded() {
        statComponent.should(visible).shouldHave(text("Statistics"));
        spendingTable.should(visible).shouldHave(text("History of Spendings"));
        return this;
    }


}
