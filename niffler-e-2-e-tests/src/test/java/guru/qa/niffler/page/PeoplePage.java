package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class PeoplePage {
    private final ElementsCollection allPeopleList = $$("#all tr");
    private final SelenideElement allTable = $x("//a[@href='/people/all']");
    private final SearchComponent searchComponent = new SearchComponent();

    public PeoplePage openPage() {
        Selenide.open(Config.getInstance().frontUrl() + "people/all");
        allTable.shouldBe(visible);
        return this;
    }

    public PeoplePage checkUserInWaitingStatus(String outcomeInviteUser) {
        searchComponent.searchUser(outcomeInviteUser);
        SelenideElement outcomeLine = allPeopleList.find(text(outcomeInviteUser));
        outcomeLine.shouldHave(text("Waiting..."));
        return this;
    }

    public PeoplePage checkNoOutcomeInvites() {
        allPeopleList.find(text("Waiting...")).shouldNot(exist);
        return this;
    }
}
