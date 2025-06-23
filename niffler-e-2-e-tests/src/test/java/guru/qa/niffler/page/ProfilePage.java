package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
	private final SelenideElement uploadNewPictureButton = $("span[role='button']");
	private final SelenideElement usernameInput = $("label[for='username']");
	private final SelenideElement nameInput = $("#name");
	private final SelenideElement saveChangesButton = $("button[type='submit']");
	private final SelenideElement addNewCategoryInput = $("#category");
	private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
	private final SelenideElement categories = $("div.MuiGrid-root.MuiGrid-container.css-17e75sl");

	public void findArchiveCategory(String categoryName) {
		showArchivedCheckbox.click();
		findActiveCategory(categoryName);
	}

	public void findActiveCategory(String categoryName) {
		categories.shouldHave(text(categoryName));
	}
}
