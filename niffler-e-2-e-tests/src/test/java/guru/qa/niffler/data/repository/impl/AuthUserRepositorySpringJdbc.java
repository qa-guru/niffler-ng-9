package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserResultSetExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;


public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.authJdbcUrl();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement userPs = con.prepareStatement(
              "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                      "VALUES (?,?,?,?,?,?)",
              Statement.RETURN_GENERATED_KEYS
      );

      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());

      userPs.executeUpdate();
      return userPs;

    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    AuthorityEntity[] authorities = user.getAuthorities().toArray(AuthorityEntity[]::new);

    jdbcTemplate.batchUpdate("INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
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
            });

    return user;

  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(con -> {
      PreparedStatement userPs = con.prepareStatement(
              """
                      UPDATE "user"
                      SET username = ?,
                          password = ?,
                          enabled = ?,
                          account_non_expired = ?,
                          account_non_locked = ?,
                          credentials_non_expired = ?
                      WHERE id = ?
                      """,
              Statement.RETURN_GENERATED_KEYS
      );

      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());
      userPs.setObject(7, user.getId());

      userPs.executeUpdate();
      return userPs;

    });

    AuthorityEntity[] authorities = user.getAuthorities().toArray(AuthorityEntity[]::new);

    jdbcTemplate.batchUpdate("""
                    UPDATE "authority"
                    SET user_id = ?,
                        authority = ?
                    WHERE id = ?
                    """,
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, authorities[i].getUser().getId());
                ps.setString(2, authorities[i].getAuthority().name());
                ps.setObject(3, authorities[i].getId());
              }

              @Override
              public int getBatchSize() {
                return authorities.length;
              }
            });

    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.of(
            jdbcTemplate.query(
                    """
                            SELECT a.id as authority_id, authority,
                            user_id as id, u.username, u.password,
                            u.enabled,
                            u.account_non_expired, u.account_non_locked, u.credentials_non_expired
                            FROM "user" u join public.authority a on u.id = a.user id WHERE u.id = ?""",
                    AuthUserResultSetExtractor.instance,
                    id
            )
    );
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.of(
            jdbcTemplate.query(
                    """
                            SELECT a.id as authority_id, authority,
                            user_id as id, a.username, u.password,
                            u.enabled,
                            u.account_non_expired, u.account_non_locked, u.credentials_non_expired
                            FROM "user" u join public.authority a on u.id = a.user id WHERE u.username = ?""",
                    AuthUserResultSetExtractor.instance,
                    username
            )
    );
  }

  @Override
  public void remove(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(con -> {
      PreparedStatement userPs = con.prepareStatement(
              "DELETE FROM \"user\" where id = ?");

      userPs.setObject(1, user.getId());

      userPs.executeUpdate();
      return userPs;

    });

    AuthorityEntity[] authorities = user.getAuthorities().toArray(AuthorityEntity[]::new);

    jdbcTemplate.batchUpdate("DELETE FROM \"authority\" where id = ?",
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, authorities[i].getId());
              }

              @Override
              public int getBatchSize() {
                return authorities.length;
              }
            });

  }
}
