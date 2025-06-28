package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;

public class AllPeoplePage {

    public static AllPeoplePage open(){
        Config CFG = Config.getInstance();
        return Selenide.open(CFG.allPeoplePageUrl(), AllPeoplePage.class);
    }
}
