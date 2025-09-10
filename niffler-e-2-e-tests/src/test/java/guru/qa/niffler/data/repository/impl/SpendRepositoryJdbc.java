package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;
import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.spendJdbcUrl();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement spendPs = holder(URL).connection().prepareStatement(
                "INSERT INTO \"spend\" (amount, category_id, currency, description, spend_date, username) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement categoryPs = holder(URL).connection().prepareStatement(
                     "INSERT INTO \"category\" (archived, name, username) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            if (spend.getCategory().getId() == null) {
                findCategoryByUsernameAndCategoryName(spend.getUsername(), spend.getCategory().getName())
                        .ifPresentOrElse(entity -> spend.setCategory(entity),
                                () -> {
                                    try {
                                        categoryPs.setString(3, spend.getCategory().getUsername());
                                        categoryPs.setString(2, spend.getCategory().getName());
                                        categoryPs.setBoolean(1, spend.getCategory().isArchived());

                                        categoryPs.executeUpdate();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    final UUID generatedKey;
                                    try (ResultSet rs = categoryPs.getGeneratedKeys()) {
                                        if (rs.next()) {
                                            generatedKey = rs.getObject("id", UUID.class);
                                        } else {
                                            throw new SQLException("Can`t find id in ResultSet");
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    spend.getCategory().setId(generatedKey);
                                });
            }

            spendPs.setDouble(1, spend.getAmount());
            spendPs.setObject(2, spend.getCategory().getId());
            spendPs.setString(3, spend.getCurrency().name());
            spendPs.setString(4, spend.getDescription());
            spendPs.setDate(5, new Date(spend.getSpendDate().getTime()));
            spendPs.setString(6, spend.getUsername());

            spendPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = spendPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            spend.setId(generatedKey);

            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        try (PreparedStatement spendPs = holder(URL).connection().prepareStatement(
                "UPDATE \"spend\" SET amount = ?, category_id = ?, currency = ?, description = ?, spend_date = ?, username = ? " +
                        "WHERE id = ?", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement categoryPs = holder(URL).connection().prepareStatement(
                     "UPDATE \"category\" SET archived = ?, name = ?, username = ? " +
                             "WHERE id = ?")) {
            spendPs.setDouble(1, spend.getAmount());
            spendPs.setObject(2, spend.getCategory().getId());
            spendPs.setString(3, spend.getCurrency().name());
            spendPs.setString(4, spend.getDescription());
            spendPs.setDate(5, new Date(spend.getSpendDate().getTime()));
            spendPs.setString(6, spend.getUsername());
            spendPs.setObject(7, spend.getId());

            spendPs.executeUpdate();

            categoryPs.setString(1, spend.getCategory().getUsername());
            categoryPs.setString(2, spend.getCategory().getName());
            categoryPs.setBoolean(3, spend.getCategory().isArchived());

            categoryPs.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO category (username, name, archived) " +
                        "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            category.setId(generatedKey);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM category WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    CategoryEntity ce = CategoryEntityRowMapper.instance.mapRow(rs, 1);
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
            try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                    "SELECT * FROM category WHERE name = ? and username = ?"
            )) {
                ps.setString(2, username);
                ps.setString(1, categoryName);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        CategoryEntity ce = CategoryEntityRowMapper.instance.mapRow(rs, 1);
                        return Optional.of(ce);
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                """
                        SELECT\s
                            spend.username,\s
                            spend.spend_date,\s
                            spend.currency,\s
                            spend.amount,\s
                            spend.description,\s
                            spend.category_id,
                            category.name,\s
                            category.archived
                        FROM spend
                        JOIN category ON spend.category_id = category.id
                        WHERE spend.id = ?
                                                
                        """
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));

                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("category_id", UUID.class));
                    ce.setUsername(rs.getString("username"));
                    ce.setName(rs.getString("name"));
                    ce.setArchived(rs.getBoolean("archived"));

                    se.setCategory(ce);
                    return Optional.of(se);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                """
                    SELECT 
                        spend.username, 
                        spend.spend_date, 
                        spend.currency, 
                        spend.amount, 
                        spend.description, 
                        spend.category_id,
                        category.name, 
                        category.archived, 
                        category.username
                    FROM spend
                    JOIN category ON spend.category_id = category.id
                    WHERE spend.username = ? AND spend.description = ?
                """
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));

                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("category_id", UUID.class));
                    ce.setUsername(rs.getString("username"));
                    ce.setName(rs.getString("name"));
                    ce.setArchived(rs.getBoolean("archived"));

                    se.setCategory(ce);
                    return Optional.of(se);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, spend.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM category WHERE id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, category.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
