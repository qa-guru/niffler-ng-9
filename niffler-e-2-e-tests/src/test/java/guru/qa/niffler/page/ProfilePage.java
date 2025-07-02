package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private String categoryLabel = ("//span[text()='new1']");
    private final SelenideElement profileLabel = $x("//h2[text()='Profile']");
    private final SelenideElement usernameInput = $("#username");

    public ProfilePage openPage() {
        Selenide.open(Config.getInstance().frontUrl() + "profile");
        return this;
    }

    public ProfilePage verifyOpened() {
        profileLabel.shouldBe(visible);
        usernameInput.shouldBe(visible);
        return this;
    }

    public boolean isCategoryShown(String categoryName) {
        SelenideElement categoryElement = $x(categoryLabel.formatted(categoryName));
        if (categoryElement.is(visible)) {
            return true;
        }
        else return false;

    }
}
