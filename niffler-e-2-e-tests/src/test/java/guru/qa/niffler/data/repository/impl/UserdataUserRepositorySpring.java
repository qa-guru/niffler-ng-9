package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityResultSetExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public class UserdataUserRepositorySpring implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)
                    VALUES (?,?,?,?,?,?,?)
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                        SELECT *
                        FROM "user" u
                        LEFT JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id
                        WHERE u.id = ?
                        """,
                        UserdataUserEntityResultSetExtractor.instance,
                        id
                )
        );
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    INSERT INTO friendship (requester_id, addressee_id, status)
                    VALUES (?, ?, ?);
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            return ps;
        }, kh);

        final java.sql.Date createdDate = (java.sql.Date) kh.getKeys().get("created_date");
        System.out.println("Generate Friendship between User: " + requester.getUsername() +
                " and User: " + addressee.getUsername() + " at " + createdDate);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    INSERT INTO friendship (requester_id, addressee_id, status)
                    VALUES (?, ?, ?);
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            return ps;
        }, kh);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        List<List<UserEntity>> userFriendshipEntries = List.of(
                List.of(requester, addressee),
                List.of(addressee, requester)
        );
        jdbcTemplate.batchUpdate(
                """
                INSERT INTO friendship (requester_id, addressee_id, status)
                VALUES (?, ?, ?);
                """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, userFriendshipEntries.get(i).get(0).getId());
                        ps.setObject(2, userFriendshipEntries.get(i).get(1).getId());
                        ps.setString(3, FriendshipStatus.ACCEPTED.name());
                    }

                    @Override
                    public int getBatchSize() {
                        return userFriendshipEntries.size();
                    }
                }
        );
    }
}
