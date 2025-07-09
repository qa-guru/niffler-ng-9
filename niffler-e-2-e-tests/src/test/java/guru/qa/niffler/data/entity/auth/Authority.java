package guru.qa.niffler.data.entity.auth;

import lombok.Getter;

@Getter
public enum Authority {
    READ("read", "Права на чтение"),
    WRITE("write", "Права на запись");

    private final String code;
    private final String description;

    Authority(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
