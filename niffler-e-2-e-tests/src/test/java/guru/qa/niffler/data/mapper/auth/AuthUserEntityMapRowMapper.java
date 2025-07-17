package guru.qa.niffler.data.mapper.auth;

import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.mapper.MapRowMapper;

import java.util.Map;
import java.util.UUID;

public class AuthUserEntityMapRowMapper<T> implements MapRowMapper<UserEntity> {

  public static final AuthUserEntityMapRowMapper instance = new AuthUserEntityMapRowMapper();

  private AuthUserEntityMapRowMapper() {
  }

  @Override
  public UserEntity mapRow(Map<String, Object> row) {
    UserEntity result = new UserEntity();
    result.setId((UUID) row.get("id"));
    result.setUsername((String) row.get("username"));
    result.setPassword((String) row.get("password"));
    result.setEnabled((Boolean) row.get("enabled"));
    result.setAccountNonExpired((Boolean) row.get("account_non_expired"));
    result.setAccountNonLocked((Boolean) row.get("account_non_locked"));
    result.setCredentialsNonExpired((Boolean) row.get("credentials_non_expired"));
    return result;
  }
}
