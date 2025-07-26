package guru.qa.niffler.data.repository.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthAuthorityRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthAuthorityRepositorySpringJdbc implements AuthAuthorityRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authorities) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.batchUpdate(
        "INSERT INTO \"authorities\" (user_id, authorities) VALUES (? , ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authorities[i].getUser().getId());
            ps.setString(2, authorities[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authorities.length;
          }
        }
    );
  }

  @Override
  public List<AuthorityEntity> findByUserId(UUID userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return jdbcTemplate.query(
        """
            SELECT
            u.id AS user_id,
            u.username,
            u.password,
            u.enabled,
            u.account_non_expired,
            u.account_non_locked,
            u.credentials_non_expired,
            a.id AS authority_id,
            a.authority
            FROM authority a
            JOIN "user" u ON u.id = a.user_id
            WHERE a.user_id = ?
            """,
        (ResultSet rs) -> {
          Map<UUID, AuthorityEntity> authorityMap = new ConcurrentHashMap<>();
          UUID authorityId = null;
          while (rs.next()) {
            authorityId = rs.getObject("authority_id", UUID.class);
            AuthorityEntity authority = authorityMap.computeIfAbsent(
                authorityId, key -> {
                  try {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("authority_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    return ae;
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });
            AuthUserEntity user = new AuthUserEntity();
            user.setId(rs.getObject("user_id", UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
            user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
            user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
            authority.setUser(user);
          }
          return new ArrayList<>(authorityMap.values());
        },
        userId
    );
  }

  @Override
  public List<AuthorityEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return jdbcTemplate.query(
        """
            SELECT
            u.id AS user_id,
            u.username,
            u.password,
            u.enabled,
            u.account_non_expired,
            u.account_non_locked,
            u.credentials_non_expired,
            a.id AS authority_id,
            a.authority
            FROM authority a
            JOIN "user" u ON u.id = a.user_id
            """,
        (ResultSet rs) -> {
          Map<UUID, AuthorityEntity> authorityMap = new ConcurrentHashMap<>();
          UUID authorityId = null;
          while (rs.next()) {
            authorityId = rs.getObject("authority_id", UUID.class);
            AuthorityEntity authority = authorityMap.computeIfAbsent(
                authorityId, key -> {
                  try {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("authority_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    return ae;
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });
            AuthUserEntity user = new AuthUserEntity();
            user.setId(rs.getObject("user_id", UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
            user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
            user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
            authority.setUser(user);
          }
          return new ArrayList<>(authorityMap.values());
        }
    );
  }

  @Override
  public void delete(AuthorityEntity authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM \"authority\" WHERE id = ?",
        authority.getId()
    );
  }
}
