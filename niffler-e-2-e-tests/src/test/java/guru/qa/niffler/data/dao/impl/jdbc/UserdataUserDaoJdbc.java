package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserDaoJdbc implements UserdataUserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UdUserEntity create(UdUserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO user (username, currency, full_name, firstname, surname, photo, photo_small) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, user.getUsername());
      ps.setObject(2, user.getCurrency());
      ps.setString(3, user.getFullname());
      ps.setString(4, user.getFirstname());
      ps.setString(5, user.getSurname());
      ps.setObject(6, user.getPhoto());
      ps.setObject(7, user.getPhotoSmall());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
        user.setId(generatedKey);
        return user;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM user WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(rs.getObject("currency", CurrencyValues.class));
          ue.setFullname(rs.getString("full_name"));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(ue);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UdUserEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM user"
    )) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        List<UdUserEntity> usersList = new ArrayList<>();
        if (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(rs.getObject("currency", CurrencyValues.class));
          ue.setFullname(rs.getString("full_name"));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          usersList.add(ue);
        } return usersList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM user WHERE username = ?"
    )) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(rs.getObject("currency", CurrencyValues.class));
          ue.setFullname(rs.getString("full_name"));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(ue);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UdUserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "DELETE FROM user WHERE id = ?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
