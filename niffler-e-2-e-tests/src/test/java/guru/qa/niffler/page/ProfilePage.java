package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.Pages.profilePage;

public class ProfilePage {
    private final SelenideElement profileIcon = $x("//*[@data-testid='PersonIcon']//ancestor::button");
    private final SelenideElement profileButton = $x("//*[text()='Profile']");
    private final SelenideElement menu = $x("//*[@data-testid='sentinelStart']/following-sibling::*[contains(@class, 'Menu')]");
    private final SelenideElement category = $x("//*[@id='category']");
    private final SelenideElement archiveButton = $x("//*[text()='Archive']");
    private final SelenideElement unarchiveButton = $x("//*[text()='Unarchive']");
    private final SelenideElement showArchivedSwitch = $x("//*[text()='Show archived']/preceding-sibling::*");

    public ProfilePage openProfile(String username) {
        profileIcon.click();
        menu.shouldBe(visible);
        profileButton.click();
        $x(String.format("//*[@id='username' and (contains(@value, '%s'))]", username)).shouldBe(visible);
        return profilePage;
    }

    public ProfilePage addCategory(String name) {
        category.setValue(name).pressEnter();
        $x(String.format("//*[text()='%s']", name)).shouldBe(visible);
        return profilePage;
    }

    public ProfilePage archiveCategory(String name) {
        $x(String.format("//*[text()='%s']/parent::*/following-sibling::*/*[contains(@aria-label, 'Archive')]", name))
                .click();
        archiveButton.click();
        $x(String.format("//*[text()='Category %s is archived']", name)).shouldBe(visible);
        return profilePage;
    }

    public ProfilePage unarchiveCategory(String name) {
        showArchivedSwitch.click();
        $x(String.format("//*[text()='%s']/parent::*/following-sibling::*/*[contains(@aria-label, 'Unarchive')]", name))
                .click();
        unarchiveButton.click();
        $x(String.format("//*[text()='Category %s is unarchived']", name)).shouldBe(visible);
        return profilePage;
    }

    public ProfilePage checkArchiveCategoryIsVisible(String name) {
        showArchivedSwitch.click();
        $x(String.format("//*[text()='%s']", name))
                .shouldBe(visible);
        return profilePage;
    }

    public ProfilePage checkCategoryIsVisible(String name) {
        $x(String.format("//*[text()='%s']", name))
                .shouldBe(visible);
        return profilePage;
    }
}
