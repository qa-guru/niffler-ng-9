package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement staticsTitle = $("//h2[text()='History of Spendings']");
    private final SelenideElement historyTitle = $x("//h2[text()='Statistics']");

    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContainsSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .should(visible);
        return this;
    }

    public MainPage checkWhatTitleStaticsIsVisible() {
        staticsTitle.shouldBe(visible);
        return this;
    }

    public MainPage checkWhatTitleHistoryIsVisible() {
        historyTitle.shouldBe(visible);
        return this;
    }
}
