package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final SelenideElement
        pageTab = $("[aria-label='People tabs']"),
        searchInput = $("input[aria-label='search']"),
        friendsTable = $("#simple-tabpanel-friends table"),
        nextPageBtn = $("#page-next"),
        prevPageBtn = $("#page-prev"),
        alert=$("#root [role='presentation']");

    public FriendsPage checkThatPageOpen(){
        pageTab.$$("a").find(href("/people/friends")).
                shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    public FriendsPage findFriend(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkThatFriendsTableIsEmpty(){
        friendsTable.$("tr").shouldNotBe(exist);
        return this;
    }

    public FriendsPage acceptFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername))
                .$$("button").find(text("Accept")).click();
        alert.shouldHave(text("Invitation of " + friendUsername + " accepted"));
        return this;
    }

    public FriendsPage declineFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername))
                .$$("button").find(text("Decline")).click();
        return this;
    }
}
