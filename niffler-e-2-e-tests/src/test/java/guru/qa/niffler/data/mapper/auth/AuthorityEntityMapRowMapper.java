package guru.qa.niffler.data.mapper.auth;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.MapRowMapper;

import java.util.Map;
import java.util.UUID;

public class AuthorityEntityMapRowMapper implements MapRowMapper<AuthorityEntity> {

  public static final AuthorityEntityMapRowMapper instance = new AuthorityEntityMapRowMapper();

  private AuthorityEntityMapRowMapper() {
  }

  @Override
  public AuthorityEntity mapRow(Map<String, Object> row) {
    AuthorityEntity ae = new AuthorityEntity();
    ae.setId((UUID) row.get("id"));
    ae.setAuthority((Authority) row.get("authority"));
    ae.setUserId((UUID) row.get("user_id"));
    return ae;
  }
}
