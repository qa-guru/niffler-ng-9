package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {
    private final SelenideElement
        pageTab = $("[aria-label='People tabs']"),
        searchInput = $("input[aria-label='search']"),
        peopleTable = $("#simple-tabpanel-all table"),
        nextPageBtn = $("#page-next"),
        prevPageBtn = $("#page-prev"),
        alert=$("#root [role='presentation']");

    public AllPeoplePage checkThatPageOpen(){
        pageTab.$$("a").find(href("/people/all")).
                shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    public AllPeoplePage findPeopleWithSearchInput(String username){
        searchInput.val(username).pressEnter();
        peopleTable.$$("td").find(text(username))
                .shouldBe(visible);
        return this;
    }

    public AllPeoplePage addFriendFromTable(String username){
        searchInput.val(username).pressEnter();
        peopleTable.$$("tr").find(text(username))
                .$$("td").find(text("Add friend")).click();
        alert.shouldHave(text("Invitation sent to " + username));
        return this;
    }
}
