package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
	private final SelenideElement uploadNewPictureButton = $("span[role='button']");
	private final SelenideElement usernameInput = $("label[for='username']");
	private final SelenideElement nameInput = $("#name");
	private final SelenideElement saveChangesButton = $("button[type='submit']");
	private final SelenideElement addNewCategoryInput = $("#category");
	private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
}
