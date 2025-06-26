package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement newPictureButton = $("[for='image__input']");
    private final SelenideElement inputUserName = $("input[name='username']");
    private final SelenideElement inputName = $("input[name='name']");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement inputCategoty = $("input[name='category']");
    private final SelenideElement archivedSwitch = $("input[type='checkbox']");
    private final ElementsCollection categories = $$("div.MuiGrid-container div.MuiGrid-grid-xs-12 ");

    public ProfilePage clickNewPictureButton() {
        newPictureButton.shouldBe(enabled).click();
        return this;
    }

    public ProfilePage clickShowArchivedSwitch() {
        archivedSwitch.shouldBe(enabled).click();
        return this;
    }

    public ArchiveModalForm clickUnarchivedButton(String category) {
        categories.find(Condition.innerText(category)).find("[aria-label='Archive category']").click();
        return page(ArchiveModalForm.class).checkOpen(category);
    }
}
