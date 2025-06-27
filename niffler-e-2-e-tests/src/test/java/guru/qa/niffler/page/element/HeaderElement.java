package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class HeaderElement {
    private final SelenideElement
        openProfileBtn = $("header button"),
        profileMenu = $("[role='menu']");

    public ProfilePage openProfilePageFromHeader(){
        openProfileBtn.click();
        profileMenu.$$("li").find(text("Profile")).click();
        return new ProfilePage();
    }

    public AllPeoplePage openAllPeoplePageFromHeader(){
        openProfileBtn.click();
        profileMenu.$$("li").find(text("All People")).click();
        return new AllPeoplePage();
    }

    public FriendsPage openFriendsPageFromHeader(){
        openProfileBtn.click();
        profileMenu.$$("li").find(text("Friends")).click();
        return new FriendsPage();
    }
}
