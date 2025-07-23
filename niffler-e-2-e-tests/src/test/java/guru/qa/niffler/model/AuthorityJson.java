package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson (
   UUID id,
   Authority authority,
   UUID userId) {

  public static AuthorityJson fromEntity(AuthorityEntity entity) {
    return new AuthorityJson(
            entity.getId(),
            entity.getAuthority(),
            entity.getUserId()
    );
  }
}
