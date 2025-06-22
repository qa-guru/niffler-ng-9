package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class UserProfilePage {

    private final SelenideElement uploadNewPictureButton = $("label[for='image__input']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");

    public UserProfilePage checkThatPageLoaded() {
        uploadNewPictureButton.shouldBe(Condition.visible);
        usernameInput.shouldBe(Condition.visible);
        nameInput.shouldBe(Condition.visible);
        submitButton.shouldBe(Condition.visible);
        showArchivedCheckbox.parent().shouldBe(Condition.visible);
        getCategoryInput("").shouldBe(Condition.visible);
        return this;
    }

    public UserProfilePage clickUploadNewPictureButton() {
        uploadNewPictureButton.click();
        return this;
    }

    public UserProfilePage checkUsername(String username) {
        usernameInput.shouldHave(value(username));
        return this;
    }

    public UserProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public UserProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    public UserProfilePage clickOnSubmitButton() {
        submitButton.click();
        return this;
    }

    public UserProfilePage clickOnShowArchivedCheckbox(boolean shouldBeShowed) {
        if (showArchivedCheckbox.isSelected() != shouldBeShowed) {
            showArchivedCheckbox.click();
        }
        return this;
    }

    public UserProfilePage addNewCategory() {
        getCategoryInput("").setValue(RandomStringUtils.randomAlphanumeric(10)).pressEnter();
        return this;
    }

    public UserProfilePage clickOnEditCategoryNameButton(String categoryName) {
        getCategoryButton(categoryName).parent().find(By.xpath("//button[@aria-label='Edit category']")).click();
        return this;
    }

    public UserProfilePage clickOnArchiveCategoryNameButton(String categoryName) {
        getCategoryButton(categoryName).parent().find(By.xpath("//button[@aria-label='Archive category']")).click();
        return this;
    }

    public UserProfilePage editCategoryName(String categoryName, String newCategoryName) {
        getCategoryInput(categoryName).setValue(newCategoryName).pressEnter();
        return this;
    }

    public UserProfilePage closeEditCategoryName(String categoryName) {
        getCategoryInput(categoryName).parent().find(By.xpath("//button[@aria-label='close']")).click();
        return this;
    }

    public UserProfilePage checkThatCategoryNonExist(String categoryName) {
        getCategoryButton(categoryName).shouldNotBe(Condition.visible);
        return this;
    }

    public UserProfilePage checkThatCategoryExist(String categoryName) {
        getCategoryButton(categoryName).shouldBe(Condition.visible);
        return this;
    }

    private SelenideElement getCategoryInput(String categoryName) {
        return $x(String.format("//input[@name='category' and @value='%s']", categoryName));
    }

    private SelenideElement getCategoryButton(String categoryName) {
        return $x(String.format("//span[text() ='%s']", categoryName));
    }
}
