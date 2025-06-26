package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

  private final SelenideElement
          spendingTable = $("#spendings"),
          statisticsFigure = $("#stat"),
          headerBlock = $("#root header");

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

  public void checkThatTableContainsSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description))
        .should(visible);
  }

  public void verifyMainPageOpened() {
    spendingTable.shouldBe(visible);
    statisticsFigure.shouldBe(visible);
    headerBlock.shouldBe(visible);
  }

  public ProfilePage navigateToProfilePage() {
    Config CFG = Config.getInstance();
    Selenide.open(CFG.profilePageUrl(), ProfilePage.class);
    return new ProfilePage();
  }
}
