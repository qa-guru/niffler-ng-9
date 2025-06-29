package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.util.XpathUtil;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private final SelenideElement profileButton = $(By.xpath("//*[@aria-label='Menu']"));
    private final SelenideElement menuList = $(By.xpath("//ul[@role='menu']"));
    private final SelenideElement profileLink = $(By.xpath("//a[@href='/profile']"));
    private final SelenideElement showArchiveToggle = $(By.xpath("//span[contains(text(), 'Show archived')]"));
    private final SelenideElement archiveButton = $(By.xpath("//button[text()= 'Archive']"));
    private final SelenideElement unarchiveButton = $(By.xpath("//button[text()= 'Unarchive']"));
    private final SelenideElement profileHeader = $(By.xpath("//h2[text()='Profile']"));


    public ProfilePage profileButtonClick() {
        profileButton.click();
        return this;
    }

    public ProfilePage profileLinkClick() {
        profileLink.click();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        showArchiveToggle.click();
        return this;
    }

    public ProfilePage archiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Archive category']", category).click();
        archiveButton.should(visible).click();
        return this;
    }

    public ProfilePage unarchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Unarchive category']", category).click();
        unarchiveButton.should(visible).click();
        return this;
    }

    public ProfilePage checkArchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Unarchive category']", category).should(visible);
        return this;
    }

    public ProfilePage checkUnarchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Archive category']", category).should(visible);
        return this;
    }

    public ProfilePage checkProfileHeader() {
        profileHeader.should(visible);
        return this;
    }

    public ProfilePage checkMenuListIsPresent() {
        menuList.should(visible);
        return this;
    }
}
