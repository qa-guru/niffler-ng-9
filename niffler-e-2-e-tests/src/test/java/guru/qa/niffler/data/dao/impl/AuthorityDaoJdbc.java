package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.UUID;

public class AuthorityDaoJdbc implements AuthorityDao {
    private final Connection connection;

    public AuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        for (AuthorityEntity authorityEntity : authority) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO authority (authority, user_id) " +
                            "VALUES (?, ?)"
            )) {
                ps.setString(1, authorityEntity.getAuthority().name());
                ps.setObject(2, authorityEntity.getUser().getId());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
