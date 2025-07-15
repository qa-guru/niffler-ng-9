package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserDao {

  private final Connection connection;

  public UserdataUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity createUser(UserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
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
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM user WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
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
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM user WHERE username = ?"
    )) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
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
  public void deleteUser(UserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM user WHERE id = ?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
