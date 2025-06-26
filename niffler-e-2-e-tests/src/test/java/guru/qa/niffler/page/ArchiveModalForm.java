package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ArchiveModalForm implements Checked<ArchiveModalForm> {

    private final SelenideElement description = $(By.id("alert-dialog-slide-description"));
    private final SelenideElement title = $("[role='dialog'] h2");
    private final SelenideElement archiveButton = $("[role='presentation'] button.MuiButton-containedPrimary");

    @Override
    public ArchiveModalForm checkOpen() {
        title.shouldBe(visible, exactText("Archive category"));
        return this;
    }

    public ArchiveModalForm checkOpen(String category) {
        this.checkOpen();
        description.shouldBe(visible, exactText("Do you really want to archive " + category +
                "? After this change it won't be available while creating spends"));
        return this;
    }

    public ArchiveModalForm clickArchiveButton() {
        archiveButton.shouldBe(enabled).click();
        return this;
    }
}
