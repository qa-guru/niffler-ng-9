package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;

public abstract class BasePage<T extends BasePage<?>> {

  protected final SelenideElement alert = Selenide.$("");

  @SuppressWarnings("unchecked")
  public T checkAlert(String text) {
    alert.shouldHave(text(text));
    return (T) this;
  }
}
