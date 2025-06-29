package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

    private final SelenideElement showArchivedSwitch = $x("//input[contains(@class, 'MuiSwitch-input')]");
    private final String categoryNameLocator = "//span[text()='%s']";


    public ProfilePage clickOnShowArchivedSwitch() {
        showArchivedSwitch.click();
        return this;
    }

    public ProfilePage checkThatCategoryNotVisible(String categoryName) {
        $x(String.format(categoryNameLocator, categoryName)).shouldNotBe(visible);
        return this;
    }

    public ProfilePage checkThatCategoryVisible(String categoryName) {
        $x(String.format(categoryNameLocator, categoryName)).shouldBe(visible);
        return this;
    }
}
