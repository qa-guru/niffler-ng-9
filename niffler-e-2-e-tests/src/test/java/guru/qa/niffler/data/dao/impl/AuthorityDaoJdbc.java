package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;

public class AuthorityDaoJdbc implements AuthAuthorityDao {
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
