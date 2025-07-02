package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement statisticsLabel = $(By.xpath("//h2[contains(text(),'Statistics')]"));
  private final SelenideElement historySpending = $(By.xpath("//h2[contains(text(),'History')]"));
  private final SelenideElement personalIcon = $("[data-testid='PersonIcon']");
  private final SelenideElement profileLink = $("[href='/profile']");

  public MainPage checkThatMainPageLoaded() {
    spendingTable.should(visible);
    return this;
  }

  public MainPage checkThatMainPageStatistics() {
    statisticsLabel.should(visible);
    return this;
  }

  public MainPage checkThatMainPageHistorySpending() {
    historySpending.should(visible);
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

  public ProfilePage navigateToProfile() {
    personalIcon.click();
    profileLink.should(visible).click();
    return new ProfilePage();
  }

}
