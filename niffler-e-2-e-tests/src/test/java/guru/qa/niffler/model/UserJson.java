package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthorityUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public record UserJson (
   UUID id,
   String username,
   String password,
   Boolean enabled,
   Boolean accountNonExpired,
   Boolean accountNonLocked,
   Boolean credentialsNonExpired,
   List<AuthorityEntity> authorities) {

  public static UserJson fromEntity(AuthorityUserEntity entity) {
    return new UserJson(
            entity.getId(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getEnabled(),
            entity.getAccountNonExpired(),
            entity.getAccountNonLocked(),
            entity.getCredentialsNonExpired(),
            entity.getAuthorities()
    );
  }
}
