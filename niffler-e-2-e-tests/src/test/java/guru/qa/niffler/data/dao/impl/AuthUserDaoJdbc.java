package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setBoolean(2, user.getAccountNonExpired());
            ps.setBoolean(3, user.getAccountNonLocked());
            ps.setBoolean(4, user.getCredentialsNonExpired());
            ps.setBoolean(5, user.getEnabled());
            ps.setString(6, pe.encode(user.getPassword()));

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
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.empty();
    }

    private AuthUserEntity getAuthUserEntityFromResultSet(ResultSet rs) throws SQLException {
        AuthUserEntity result = new AuthUserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setPassword(rs.getString("password"));
        result.setEnabled(rs.getBoolean("enabled"));
        result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return result;
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * from user")){
            ps.execute();
            List<AuthUserEntity> resultList = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity ce = getAuthUserEntityFromResultSet(rs);
                    resultList.add(ce);
                }
                return resultList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
