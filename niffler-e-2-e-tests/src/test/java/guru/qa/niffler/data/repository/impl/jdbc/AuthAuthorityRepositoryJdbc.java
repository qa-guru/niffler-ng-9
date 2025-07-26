package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthAuthorityRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityRepositoryJdbc implements AuthAuthorityRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authorities) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"authorities\" (user_id, authorities) VALUES (?, ?)")) {
      for (AuthorityEntity a : authorities) {
        ps.setObject(1, a.getUser());
        ps.setString(2, a.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findByUserId(UUID userId) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
            """
    )) {
      ps.setObject(1, userId);
      ps.execute();
      List<AuthorityEntity> authorityEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("authority_id", UUID.class));
          ae.setAuthority((Authority.valueOf(rs.getString("authority"))));
          if (ae == null) {
            return new ArrayList<>();
          }
          AuthUserEntity user = new AuthUserEntity();
          user.setId(rs.getObject("user_id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          ae.setUser(user);
          authorityEntities.add(ae);
        }
      }
      return authorityEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
            """
    )) {
      ps.execute();
      List<AuthorityEntity> authorityEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("authority_id", UUID.class));
          ae.setAuthority((Authority.valueOf(rs.getString("authority"))));
          if (ae == null) {
            return new ArrayList<>();
          }
          AuthUserEntity user = new AuthUserEntity();
          user.setId(rs.getObject("user_id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          ae.setUser(user);
          authorityEntities.add(ae);
        }
      }
      return authorityEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthorityEntity authority) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "DELETE FROM user WHERE id = ?"
    )) {
      ps.setObject(1, authority.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
