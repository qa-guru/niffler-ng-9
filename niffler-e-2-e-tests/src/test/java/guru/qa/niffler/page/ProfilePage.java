package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
    private final ElementsCollection categoryList = $$("[class*='MuiChip-labelMedium']");

    public ProfilePage showArchivedCategories() {
        showArchivedCheckbox.click();
        return this;
    }

    public void checkCategoryInCategoryList(String categoryName) {
        categoryList.findBy(text(categoryName)).should(visible);
    }
}
