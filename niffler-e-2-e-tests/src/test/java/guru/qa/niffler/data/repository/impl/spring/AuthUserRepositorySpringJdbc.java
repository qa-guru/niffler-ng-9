package guru.qa.niffler.data.repository.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement userPs = con.prepareStatement(
          "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());
      return userPs;
    }, kh);
    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);

    List<AuthorityEntity> authority = Stream.of(
        new AuthorityEntity(Authority.write, user),
        new AuthorityEntity(Authority.read, user)
    ).toList();
    jdbcTemplate.batchUpdate(
        "INSERT INTO \"authority\" (user_id, authority) VALUES (? , ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority.get(i).getUser().getId());
            ps.setString(2, authority.get(i).getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.size();
          }
        }
    );
    user.setAuthorities(authority);

    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.query(
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
                FROM "user" u
                JOIN authority a ON u.id = a.user_id
                WHERE u.id = ?
                """,
            (ResultSet rs) -> {
              Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
              UUID userId = null;
              while (rs.next()) {
                userId = rs.getObject("user_id", UUID.class);
                AuthUserEntity user = userMap.computeIfAbsent(
                    userId, key -> {
                      try {
                        AuthUserEntity ue = new AuthUserEntity();
                        ue.setId(key);
                        ue.setUsername(rs.getString("username"));
                        ue.setPassword(rs.getString("password"));
                        ue.setEnabled(rs.getBoolean("enabled"));
                        ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                        ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                        ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                        return ue;
                      } catch (SQLException e) {
                        throw new RuntimeException(e);
                      }
                    });
                AuthorityEntity authority = new AuthorityEntity();
                authority.setId(rs.getObject("authority_id", UUID.class));
                authority.setUser(user);
                authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                user.getAuthorities().add(authority);
              }
              return userMap.get(userId);
            },
            id
        )
    );
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.query(
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
                FROM "user" u
                JOIN authority a ON u.id = a.user_id
                WHERE u.username = ?
                """,
            (ResultSet rs) -> {
              Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
              UUID userId = null;
              while (rs.next()) {
                userId = rs.getObject("user_id", UUID.class);
                AuthUserEntity user = userMap.computeIfAbsent(
                    userId, key -> {
                      try {
                        AuthUserEntity ue = new AuthUserEntity();
                        ue.setId(key);
                        ue.setUsername(rs.getString("username"));
                        ue.setPassword(rs.getString("password"));
                        ue.setEnabled(rs.getBoolean("enabled"));
                        ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                        ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                        ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                        return ue;
                      } catch (SQLException e) {
                        throw new RuntimeException(e);
                      }
                    });
                AuthorityEntity authority = new AuthorityEntity();
                authority.setId(rs.getObject("authority_id", UUID.class));
                authority.setUser(user);
                authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                user.getAuthorities().add(authority);
              }
              return userMap.get(userId);
            },
            username
        )
    );
  }

  @Override
  public List<AuthUserEntity> findAll() {
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
            FROM "user" u
            JOIN authority a ON u.id = a.user_id
            """,
        (ResultSet rs) -> {
          Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
          while (rs.next()) {
            UUID userId = rs.getObject("user_id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(
                userId, key -> {
                  try {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(key);
                    ue.setUsername(rs.getString("username"));
                    ue.setPassword(rs.getString("password"));
                    ue.setEnabled(rs.getBoolean("enabled"));
                    ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return ue;
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setUser(user);
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            user.getAuthorities().add(authority);
          }
          return new ArrayList<>(userMap.values());
        }
    );

  }

  @Override
  public void delete(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM \"user\" WHERE id = ?",
        user.getId()
    );
  }
}
