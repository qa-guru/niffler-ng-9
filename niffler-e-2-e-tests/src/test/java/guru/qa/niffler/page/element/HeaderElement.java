package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class HeaderElement {
    private final SelenideElement
        nifflerLogo = $(""),
        openProfileBtn = $(""),
        profileMenu = $("[role='menu']");

    public ProfilePage openProfilePageFromHeader(){
        openProfileBtn.click();
        profileMenu.$$("li").find(text("Profile")).click();
        return new ProfilePage();
    }

    public MainPage openMainPageFromHeader(){
        nifflerLogo.click();
        return new MainPage();
    }
}
