package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.AuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {
  private UUID id;
  private Authority authority;
  private AuthorityUserEntity user;

  public static AuthorityEntity fromJson(AuthorityJson authorityJson) {
    AuthorityEntity ae = new AuthorityEntity();
    ae.id = authorityJson.id();
    ae.authority = authorityJson.authority();
    ae.user = authorityJson.user();
    return ae;
  }
}
