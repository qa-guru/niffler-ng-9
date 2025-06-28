package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement
            friendsTab = $("a[href*='friends']"),
            allPeopleTab = $("a[href*='people/all']");

    public static FriendsPage open(){
        Config CFG = Config.getInstance();
        return Selenide.open(CFG.friendsPageUrl(), FriendsPage.class);
    }

}
