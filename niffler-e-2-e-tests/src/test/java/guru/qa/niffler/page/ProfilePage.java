package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveButton = $("button[type='submit']");
    private final SelenideElement categoriesContainer = $$("div.MuiGrid-container").get(1);
    private final ElementsCollection categoriesItems = categoriesContainer.$$(".MuiGrid-item");
    private final SelenideElement showArchivedSwitcher = $(".MuiSwitch-switchBase");
    private int initialSize;


    public ProfilePage fillProfileName(String name) {
        nameInput.clear();
        nameInput.setValue(name);

        return this;
    }

    public ProfilePage save() {
        saveButton.click();
        return new ProfilePage();
    }

    public ProfilePage checkCategoryContainerVisible() {
        categoriesContainer.should(visible);
        return this;
    }

    public ProfilePage getCategoryItemsCount() {
        this.initialSize = categoriesItems.size();
        return this;
    }

    public ProfilePage checkArchivedSwitcherUnchecked() {
        showArchivedSwitcher.shouldNotHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage checkArchivedSwitcherChecked() {
        showArchivedSwitcher.shouldHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage clickShowArchivedSwitcher() {
        showArchivedSwitcher.should(visible).click();
        return new ProfilePage();
    }

    public ProfilePage checkCategoryCountIncreased() {
        categoriesItems.shouldHave(sizeGreaterThan(initialSize));
        return this;
    }

    public void checkArchivedCategoryVisible(String categoryName) {
        $(byText(categoryName)).should(visible);
    }

    public void checkActiveCategoryVisible(String categoryName) {
        $(byText(categoryName)).should(visible);
    }
}
