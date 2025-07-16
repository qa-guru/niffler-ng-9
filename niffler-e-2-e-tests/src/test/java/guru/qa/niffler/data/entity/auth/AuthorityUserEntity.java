package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthorityUserEntity implements Serializable {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public static AuthorityUserEntity fromJson(UserJson userJson) {
    AuthorityUserEntity ue = new AuthorityUserEntity();
    ue.id = userJson.getId();
    ue.username = userJson.getUsername();
    ue.password = userJson.getPassword();
    ue.enabled = userJson.getEnabled();
    ue.accountNonExpired = userJson.getAccountNonExpired();
    ue.accountNonLocked = userJson.getAccountNonLocked();
    ue.credentialsNonExpired = userJson.getCredentialsNonExpired();
    ue.authorities = userJson.getAuthorities();
    return ue;
  }
}
