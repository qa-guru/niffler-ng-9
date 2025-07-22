package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {

  public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

  private AuthUserEntityExtractor() {
  }

  @Override
  public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
    return null;
  }
}
