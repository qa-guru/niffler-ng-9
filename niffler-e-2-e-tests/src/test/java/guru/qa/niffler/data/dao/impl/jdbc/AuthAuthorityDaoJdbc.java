package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authority) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
      for (AuthorityEntity a : authority) {
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
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUser(rs.getObject("userId", AuthUserEntity.class));
          ae.setAuthority(rs.getObject("authority", Authority.class));
          return Optional.of(ae);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findByUserId(UUID userId) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM authority WHERE userId = ?"
    )) {
      ps.setObject(1, userId);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUser(rs.getObject("userId", AuthUserEntity.class));
          ae.setAuthority(rs.getObject("authority", Authority.class));
          return Optional.of(ae);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority"
    )) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        List<AuthorityEntity> authorityList = new ArrayList<>();
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUser(rs.getObject("userId", AuthUserEntity.class));
          ae.setAuthority(rs.getObject("authority", Authority.class));
          authorityList.add(ae);
        }
        return authorityList;
      }
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
