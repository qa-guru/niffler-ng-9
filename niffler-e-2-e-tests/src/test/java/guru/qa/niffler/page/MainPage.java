package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.element.HeaderElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final HeaderElement headerElement = new HeaderElement();

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

  public ProfilePage openProfilePageFromHeader(){
    headerElement.openProfilePageFromHeader();
    return new ProfilePage();
  }

  public FriendsPage openFriendsPageFromHeader(){
    headerElement.openFriendsPageFromHeader();
    return new FriendsPage();
  }

  public AllPeoplePage openAllPeoplePageFromHeader(){
    headerElement.openAllPeoplePageFromHeader();
    return new AllPeoplePage();
  }
}
