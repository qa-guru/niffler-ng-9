package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.Pages.profilePage;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement profileIcon = $x("//*[@data-testid='PersonIcon']//ancestor::button");
    private final SelenideElement profileButton = $x("//*[text()='Profile']");
    private final SelenideElement menu = $x("//*[@data-testid='sentinelStart']/following-sibling::*[contains(@class, 'Menu')]");
    private final SelenideElement category = $x("//*[@id='category']");
    private final SelenideElement archiveButton = $x("//*[text()='Archive']");
    private final SelenideElement unarchiveButton = $x("//*[text()='Unarchive']");
    private final SelenideElement showArchivedSwitch = $x("//*[text()='Show archived']/preceding-sibling::*");
  private final SelenideElement avatar = $("#image__input").parent().$("img");
  private final SelenideElement userName = $("#username");
  private final SelenideElement nameInput = $("#name");
  private final SelenideElement photoInput = $("input[type='file']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
  private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
  private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");


    public ProfilePage openProfile(String username) {
        profileIcon.click();
        menu.shouldBe(visible);
        profileButton.click();
        $x(String.format("//*[@id='username' and (contains(@value, '%s'))]", username)).shouldBe(visible);
        return this;
    }
  public ProfilePage setName(String name) {
    nameInput.clear();
    nameInput.setValue(name);
    return this;
  }

    public ProfilePage addCategory(String name) {
        category.setValue(name).pressEnter();
        $x(String.format("//*[text()='%s']", name)).shouldBe(visible);
        return this;
    }
  public ProfilePage uploadPhotoFromClasspath(String path) {
    photoInput.uploadFromClasspath(path);
    return this;
  }

    public ProfilePage archiveCategory(String name) {
        $x(String.format("//*[text()='%s']/parent::*/following-sibling::*/*[contains(@aria-label, 'Archive')]", name))
                .click();
        archiveButton.click();
        $x(String.format("//*[text()='Category %s is archived']", name)).shouldBe(visible);
        return this;
    }

    public ProfilePage unarchiveCategory(String name) {
        showArchivedSwitch.click();
        $x(String.format("//*[text()='%s']/parent::*/following-sibling::*/*[contains(@aria-label, 'Unarchive')]", name))
                .click();
        unarchiveButton.click();
        $x(String.format("//*[text()='Category %s is unarchived']", name)).shouldBe(visible);
        return this;
    }
  public ProfilePage checkCategoryExists(String category) {
    bubbles.find(text(category)).shouldBe(visible);
    return this;
  }

    public ProfilePage checkArchiveCategoryIsVisible(String name) {
        showArchivedSwitch.click();
        $x(String.format("//*[text()='%s']", name))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage checkCategoryIsVisible(String name) {
        $x(String.format("//*[text()='%s']", name))
                .shouldBe(visible);
        return this;
    }
  public ProfilePage checkUsername(String username) {
    this.userName.should(value(username));
    return this;
  }

  public ProfilePage checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  public ProfilePage checkPhotoExist() {
    avatar.should(attributeMatching("src", "data:image.*"));
    return this;
  }

  public ProfilePage checkThatCategoryInputDisabled() {
    categoryInput.should(disabled);
    return this;
  }

  public ProfilePage submitProfile() {
    submitButton.click();
    return this;
  }

    public ProfilePage checkArchivedCategoryExists(String category) {
        archivedSwitcher.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }
}
