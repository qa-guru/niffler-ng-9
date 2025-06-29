package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

public class PeoplePage {

    public static PeoplePage open() {
        Config CFG = Config.getInstance();
        return Selenide.open(CFG.allPeoplePageUrl(), PeoplePage.class);
    }

    public void checkOutcomeFriendRequest(String outcomeUserName){
        String locator = format("//tr[.//p[contains(@class, 'MuiTypography-body') and text()='%s']]", outcomeUserName);
        $x(locator).shouldHave(text("Waiting..."));
    }
}
