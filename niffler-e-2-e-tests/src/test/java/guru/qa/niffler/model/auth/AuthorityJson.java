package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
    UUID id,
    UUID userId,
    Authority authority) {

  public static AuthorityJson fromEntity(AuthorityEntity entity) {
    return new AuthorityJson(
        entity.getId(),
        entity.getUser().getId(),
        entity.getAuthority()
    );
  }
}
