package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;

  public static AuthUserEntity fromJson(AuthUserJson json) {
    AuthUserEntity ue = new AuthUserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setPassword(json.password());
    ue.setEnabled(json.enabled());
    ue.setAccountNonExpired(json.accountNonExpired());
    ue.setAccountNonLocked(json.accountNonLocked());
    ue.setCredentialsNonExpired(json.credentialsNonExpired());
    return ue;
  }
}
