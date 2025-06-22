package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record DeleteSpendingJson(
        @JsonProperty("ids")
        List<UUID> id) {
}
