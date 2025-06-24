package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement
        profileInfoForm = $("main div form"),
        categoriesForm = $x("//*/main/div/div");

    public ProfilePage checkUsername(String username){
        profileInfoForm.$("username").shouldHave(text(username));
        return this;
    }

    public ProfilePage setUserName(String name){
        profileInfoForm.$("name").val(name);
        profileInfoForm.$("button").click();
        return this;
    }

    public ProfilePage createNewCategory(String categoryName){
        categoriesForm.$("form #category").val(categoryName)
                .pressEnter();
        return this;
    }

    public ProfilePage showArchivedCategories(){
        categoriesForm.$("div")
                .$("label").click();
        return this;
    }

    //TODO check locator when test will be written
    public ProfilePage editCategoryName(String categoryName, String newCategoryName){
        categoriesForm.$$("div").find(text(categoryName))
                .$("[aria-label='Edit category']").click();
        categoriesForm.$$("div").find(text(categoryName))
                .val(newCategoryName).pressEnter();
        return this;
    }

    //TODO check locator when test will be written
    public ProfilePage archiveCategory(String categoryName){
        categoriesForm.$$("div").find(text(categoryName))
                .$("[aria-label='Archive category']").click();
        return this;
    }

    public ProfilePage checkCategory(String categoryName){
        categoriesForm.$$("div").find(text(categoryName))
                .should(visible);
        return this;
    }

    public ProfilePage checkArchivedCategory(String categoryName){
        categoriesForm.$$("div").find(text(categoryName))
                .should(visible);
        return this;
    }
}
