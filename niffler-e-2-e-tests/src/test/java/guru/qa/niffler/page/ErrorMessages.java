package guru.qa.niffler.page;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal"),
    BAD_CREDENTIALS("Bad credentials");

    private final String text;

    ErrorMessages(String text) {
        this.text = text;
    }
}
