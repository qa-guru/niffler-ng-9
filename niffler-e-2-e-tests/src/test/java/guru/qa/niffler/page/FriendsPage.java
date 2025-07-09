package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final ElementsCollection requestsList = $$("#requests tr");
    private final ElementsCollection friendsList = $$("#friends tr");
    private final SelenideElement friendsTable = $x("//a[@href='/people/friends']");


    public FriendsPage openPage() {
        Selenide.open(Config.getInstance().frontUrl() + "people/friends");
        friendsTable.shouldBe(visible);
        return this;
    }

    public FriendsPage checkRequestsContainUsers(List<String> requests) {
        requestsList.shouldHave(textsInAnyOrder(requests));
        return this;
    }

    public FriendsPage checkFriendsContainUsers(List<String> friends) {
        friendsList.shouldHave(textsInAnyOrder(friends));
        return this;
    }

    public FriendsPage checkFriendsEmpty() {
        friendsList.shouldHave(size(0));
        return this;
    }
}
