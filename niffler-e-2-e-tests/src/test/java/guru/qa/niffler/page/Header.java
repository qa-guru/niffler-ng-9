package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class Header {
    private SelenideElement menuButton = $("button[aria-label='Menu']");
    private SelenideElement profileLink = $("a[href='/profile']");

    public Header openMenu() {
        menuButton.click();
        return this;
    }

    public UserProfilePage clickOnProfileButton() {
        profileLink.click();
        return new UserProfilePage();
    }
}
