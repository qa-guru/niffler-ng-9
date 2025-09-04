package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

/**
 * @author Alexander
 */
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                    """
                    INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small)
                    VALUES ( ?, ?, ?, ?, ?, ?);
                """,
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(@NonNull UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                        """
                        SELECT *
                        FROM "user" u
                        LEFT LOIN friendship f ON u.id = f.requester_id or u.id = f.addressee_id
                        WHERE u.id = ?
                    """
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                UserEntity user = null;
                List<FriendshipEntity> friendshipRequests = new ArrayList<>();
                List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

                while (rs.next()) {
                    if (user == null) {
                        user = UserdataUserEntityRowMapper.instance.mapRow(rs, 1);
                    }
                    UUID requesterId = rs.getObject("requester_id", UUID.class);
                    UUID addresseeId = rs.getObject("addressee_id", UUID.class);
                    FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(rs.getString("status"));
                    java.sql.Date createdDate = rs.getDate("created_date");

                    UserEntity requesterUser = new UserEntity();
                    UserEntity addresseeUser = new UserEntity();
                    requesterUser.setId(requesterId);
                    addresseeUser.setId(addresseeId);

                    FriendshipEntity friendshipRequest = new FriendshipEntity();
                    FriendshipEntity friendshipAddressee = new FriendshipEntity();

                    friendshipRequest.setRequester(requesterUser);
                    friendshipRequest.setAddressee(addresseeUser);
                    friendshipRequest.setCreatedDate(createdDate);
                    friendshipRequest.setStatus(friendshipStatus);

                    friendshipAddressee.setRequester(addresseeUser);
                    friendshipAddressee.setAddressee(requesterUser);
                    friendshipAddressee.setCreatedDate(createdDate);
                    friendshipAddressee.setStatus(friendshipStatus);

                    if (id.equals(requesterId)) {
                        friendshipRequests.add(friendshipRequest);
                    } else {
                        friendshipAddressees.add(friendshipAddressee);
                    }
                }
                if (user == null) {
                    return Optional.empty();
                }
                else {
                    user.setFriendshipRequests(friendshipRequests);
                    user.setFriendshipAddressees(friendshipAddressees);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                        """
                        INSERT INTO friendship (requester_id, addressee_id, status)
                        VALUES (?, ?, ?);
                    """
        )) {
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.executeUpdate();

            final java.sql.Date createdDate;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    createdDate = rs.getDate("created_date");
                    System.out.println("Generate Income Invitation to User: " + requester.getUsername() +
                            " from User: " + addressee.getUsername() + " at " + createdDate);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                    """
                    INSERT INTO friendship (requester_id, addressee_id, status)
                    VALUES (?, ?, ?);
                    """
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.executeUpdate();

            final java.sql.Date createdDate;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    createdDate = rs.getDate("created_date");
                    System.out.println("Generate Outcome Invitation from User: " + requester.getUsername() +
                            " to User: " + addressee.getUsername() + " at " + createdDate);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                """
                INSERT INTO friendship (requester_id, addressee_id, status)
                VALUES (?, ?, ?);
                """,
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.addBatch();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.addBatch();

            ps.executeBatch();

            final java.sql.Date createdDate;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    createdDate = rs.getDate("created_date");
                    System.out.println("Generate Friendship between User: " + requester.getUsername() +
                            " and User: " + addressee.getUsername() + " at " + createdDate);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
