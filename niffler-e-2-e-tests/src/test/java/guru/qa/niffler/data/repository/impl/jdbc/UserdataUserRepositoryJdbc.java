package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.userdata.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  private final AuthUserRepository userRepository = new AuthUserRepositoryJdbc();

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
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    ); PreparedStatement addresseesPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.addressee_id
            WHERE f.requester_id = ?
            """
    ); PreparedStatement requestersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.requester_id
            WHERE f.addressee_id = ?
            """
    )) {
      userPs.setObject(1, id);
      userPs.execute();
      UdUserEntity user = null;
      try (ResultSet userRs = userPs.getResultSet()) {
        if (userRs.next()) {
          user = UserdataUserEntityRowMapper.instance.mapRow(userRs, 1);
        }
      }
      if (user == null) {
        return Optional.empty();
      }
      user.setFriendshipRequests(loadFriendships(requestersPs, user.getId(), true, user));
      user.setFriendshipAddressees(loadFriendships(addresseesPs, user.getId(), false, user));
      return Optional.of(user);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UdUserEntity> findAll() {
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\""
    ); PreparedStatement addresseesPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.addressee_id
            WHERE f.requester_id = ?
            """
    ); PreparedStatement requestersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.requester_id
            WHERE f.addressee_id = ?
            """
    )) {
      userPs.execute();
      try (ResultSet userRs = userPs.getResultSet()) {
        List<UdUserEntity> usersList = new ArrayList<>();
        while (userRs.next()) {
          UdUserEntity user = UserdataUserEntityRowMapper.instance.mapRow(userRs, userRs.getRow());
          user.setFriendshipRequests(loadFriendships(requestersPs, user.getId(), true, user));
          user.setFriendshipAddressees(loadFriendships(addresseesPs, user.getId(), false, user));
          usersList.add(user);
        }
        if (usersList.isEmpty()) {
          return new ArrayList<>();
        }
        return usersList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\" WHERE username = ?"
    ); PreparedStatement addresseesPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.addressee_id
            WHERE f.requester_id = ?
            """
    ); PreparedStatement requestersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT
            f.status,
            f.created_date,
            u.*
            FROM friendship f
            JOIN "user" u ON u.id = f.requester_id
            WHERE f.addressee_id = ?
            """
    )) {
      userPs.setObject(1, username);
      userPs.execute();
      UdUserEntity user = null;
      try (ResultSet userRs = userPs.getResultSet()) {
        if (userRs.next()) {
          user = UserdataUserEntityRowMapper.instance.mapRow(userRs, 1);
        }
      }
      if (user == null) {
        return Optional.empty();
      }
      user.setFriendshipRequests(loadFriendships(requestersPs, user.getId(), true, user));
      user.setFriendshipAddressees(loadFriendships(addresseesPs, user.getId(), false, user));
      return Optional.of(user);
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

  @Override
  public UdUserEntity update(UdUserEntity user) {
    try (PreparedStatement usersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            """
                  UPDATE "user"
                    SET currency    = ?,
                        firstname   = ?,
                        surname     = ?,
                        photo       = ?,
                        photo_small = ?
                    WHERE id = ?
                """);

         PreparedStatement friendsPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                 """
                     INSERT INTO friendship (requester_id, addressee_id, status)
                     VALUES (?, ?, ?)
                     ON CONFLICT (requester_id, addressee_id)
                         DO UPDATE SET status = ?
                     """)
    ) {
      usersPs.setString(1, user.getCurrency().name());
      usersPs.setString(2, user.getFirstname());
      usersPs.setString(3, user.getSurname());
      usersPs.setBytes(4, user.getPhoto());
      usersPs.setBytes(5, user.getPhotoSmall());
      usersPs.setObject(6, user.getId());
      usersPs.executeUpdate();

      for (FriendshipEntity fe : user.getFriendshipRequests()) {
        friendsPs.setObject(1, user.getId());
        friendsPs.setObject(2, fe.getAddressee().getId());
        friendsPs.setString(3, fe.getStatus().name());
        friendsPs.setString(4, fe.getStatus().name());
        friendsPs.addBatch();
        friendsPs.clearParameters();
      }
      friendsPs.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (addressee_id, requester_id, created_date, status) " +
            "VALUES (?, ?, ?, ?)"
    )) {
      ps.setObject(1, addressee.getId());
      ps.setObject(2, requester.getId());
      ps.setDate(3, new Date(System.currentTimeMillis()));
      ps.setObject(4, PENDING);
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (addressee_id, requester_id, created_date, status) " +
            "VALUES (?, ?, ?, ?), (?, ?, ?, ?)"
    )) {
      ps.setObject(1, addressee.getId());
      ps.setObject(2, requester.getId());
      ps.setDate(3, new Date(System.currentTimeMillis()));
      ps.setObject(4, ACCEPTED);
      ps.setObject(5, requester.getId());
      ps.setObject(6, addressee.getId());
      ps.setDate(7, new Date(System.currentTimeMillis()));
      ps.setObject(8, ACCEPTED);
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<FriendshipEntity> loadFriendships(PreparedStatement ps, UUID id, boolean isRequester, UdUserEntity self) throws SQLException {
    ps.setObject(1, id);
    List<FriendshipEntity> feList = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        UdUserEntity other = UserdataUserEntityRowMapper.instance.mapRow(rs, rs.getRow());

        FriendshipEntity fe = new FriendshipEntity();
        if (isRequester) {
          fe.setRequester(other);
          fe.setAddressee(self);
        } else {
          fe.setRequester(self);
          fe.setAddressee(other);
        }
        fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
        fe.setCreatedDate(rs.getDate("created_date"));
        feList.add(fe);
      }
      return feList;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
