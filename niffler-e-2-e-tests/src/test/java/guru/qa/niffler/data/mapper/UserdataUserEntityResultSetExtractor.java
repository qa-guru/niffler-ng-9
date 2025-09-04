package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexander
 */
public class UserdataUserEntityResultSetExtractor implements ResultSetExtractor<UserEntity> {

    public static final UserdataUserEntityResultSetExtractor instance = new UserdataUserEntityResultSetExtractor();

    private UserdataUserEntityResultSetExtractor() {
    }

    @Override
    public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, UserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        UserEntity user = null;
        List<FriendshipEntity> friendshipRequests = new ArrayList<>();
        List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            if (user == null) {
                user = userMap.computeIfAbsent(userId, id ->
                {
                    try {
                        return UserdataUserEntityRowMapper.instance.mapRow(rs, 1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
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

            if (userId.equals(requesterId)) {
                friendshipRequests.add(friendshipRequest);
            } else {
                friendshipAddressees.add(friendshipAddressee);
            }
        }
        user.setFriendshipRequests(friendshipRequests);
        user.setFriendshipAddressees(friendshipAddressees);
        return userMap.get(userId);
    }
}
