package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");
  private final SelenideElement profileButton = $("a[href='/profile']");

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

  public ProfilePage clickProfileButton() {
    menuButton.should(visible).click();
    profileButton.should(visible).click();
    return new ProfilePage();
  }
}
