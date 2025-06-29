package guru.qa.niffler.util;

import com.codeborne.selenide.SelenideElement;
import lombok.experimental.UtilityClass;

import static com.codeborne.selenide.Selenide.$x;

@UtilityClass
public class XpathUtil {

    public SelenideElement findByXpathTemplate(String xpathTemplate, Object... args) {
        return $x(String.format(xpathTemplate, args));
    }
}
