package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private static final SelenideElement categoriesTable = $("div.MuiGrid-container");
    private static final SelenideElement switchArchiveElement = $("span.MuiSwitch-root");
    private static final ElementsCollection categoryItems = $$("div.MuiChip-root");
    private static final SelenideElement archiveButton = $("button[aria-label='Archive category']");
    private static final SelenideElement unarchiveButton = $("button[aria-label='Unarchive category']");

    private static final SelenideElement dialogWindow = $("div.MuiDialog-paper");
    private static final SelenideElement dialogTitle = dialogWindow.$("h2.MuiTypography-h6");
    private static final SelenideElement dialogSubmitButton = dialogWindow.$("button.MuiButton-containedPrimary");


    public ProfilePage checkThatPageLoaded() {
        categoriesTable.should(visible);
        return this;
    }

    public ProfilePage switchArchiveVision() {
        switchArchiveElement.click();
        return this;
    }

    public ProfilePage archiveFirstCat() {
        archiveButton.click();
        dialogWindow.shouldBe(visible);
        dialogTitle.shouldHave(text("Archive category"));
        dialogSubmitButton
                .shouldBe(visible, enabled)
                .click();
        return this;
    }

    public ProfilePage unarchiveCat() {
        unarchiveButton.click();
        dialogWindow.shouldBe(visible);
        dialogTitle.shouldHave(text("Unarchive category"));
        dialogSubmitButton
                .shouldBe(visible, enabled)
                .click();
        return this;
    }

    public ProfilePage checkToIncTableRowsAfterUnarchiveCategory() {
        int initialCount = categoryItems.size();
        switchArchiveVision();
        unarchiveCat();
        switchArchiveVision();
        categoryItems.shouldHave(CollectionCondition.size(initialCount ));
        return this;
    }

    public ProfilePage checkToReduceTableRowsAfterArchiveTopCategory() {
        int initialCount = categoryItems.size();
        archiveFirstCat();
        categoryItems.shouldHave(CollectionCondition.size(initialCount - 1));
        return this;
    }
}
