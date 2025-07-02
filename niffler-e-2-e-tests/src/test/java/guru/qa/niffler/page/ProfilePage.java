package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
  //http://127.0.0.1:3000/profile
  private final SelenideElement profileLabel = $x("//h2[text()='Profile']");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement archivedSwitcher = $("span.MuiSwitch-thumb.css-19gndve");
  private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
  private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

  private final SelenideElement saveChangesButton = $("[type='submit']");
  private final SelenideElement editCategoryButton = $("[aria-label='Edit category']");
  private final SelenideElement archivedCategoryButton = $("[aria-label='Archive category']");
  private final SelenideElement unarchivedCategoryButton = $("[aria-label='Unarchive category']");
  private final SelenideElement archiveButton = $x("//button[text()='Archive']");
  private final SelenideElement unarchiveButton = $x("//button[text()='Unarchive']");
  private final SelenideElement uploadNewPictureButton = $x("//span[text()='Upload new picture']");
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement nameInput = $("#name");

  public ProfilePage addNewCategory(String newCategory) {
    categoryInput.setValue(newCategory).pressEnter();
    return this;
  }

  public ProfilePage checkThatItIsProfilePage() {
    profileLabel.should(visible);
    return this;
  }

  public ProfilePage checkCategoryExists(String category) {
    bubbles.find(text(category)).shouldBe(visible);
    return this;
  }

  public ProfilePage checkArchivedCategoryExists(String category) {
    archivedSwitcher.click();
    bubblesArchived.find(text(category)).shouldBe(visible);
    return this;
  }

  public SelenideElement getCategorySelector(String category) {
    String categorySelector = String.format("//span[text()='%s']", category);
    return $x(categorySelector);
  }
}
