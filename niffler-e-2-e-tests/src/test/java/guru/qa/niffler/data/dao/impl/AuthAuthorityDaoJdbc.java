package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
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
                ps.setObject(2, authorityEntity.getUserId().getId());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private AuthorityEntity getAuthEntityFromResultSet(ResultSet rs) throws SQLException {
        AuthorityEntity result = new AuthorityEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setAuthority(Authority.valueOf(rs.getString("authority")));
        AuthUserEntity user = new AuthUserEntity();
        user.setId(rs.getObject("user_id", UUID.class));
        result.setUserId(user);
        return result;
    }

    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * from authority")){
            ps.execute();
            List<AuthorityEntity> resultList = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ce = getAuthEntityFromResultSet(rs);
                    resultList.add(ce);
                }
                return resultList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
