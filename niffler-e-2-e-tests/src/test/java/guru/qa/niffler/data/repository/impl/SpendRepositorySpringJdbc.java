package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.spendJdbcUrl();

    @Override
    public SpendEntity create(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder khSpend = new GeneratedKeyHolder();
        KeyHolder khCategory = new GeneratedKeyHolder();

        if (spend.getCategory().getId() == null) {
            findCategoryByUsernameAndCategoryName(spend.getUsername(), spend.getCategory().getName())
                    .ifPresentOrElse(entity -> spend.setCategory(entity),
                            () -> {
                                jdbcTemplate.update(con -> {
                                    PreparedStatement categoryPs = con.prepareStatement(
                                            "INSERT INTO category (archived, name, username) VALUES (?, ?, ?)",
                                            Statement.RETURN_GENERATED_KEYS
                                    );

                                    categoryPs.setString(3, spend.getCategory().getUsername());
                                    categoryPs.setString(2, spend.getCategory().getName());
                                    categoryPs.setBoolean(1, spend.getCategory().isArchived());
                                    return categoryPs;
                                }, khCategory);

                                final UUID generatedKey = (UUID) khCategory.getKeys().get("id");
                                spend.getCategory().setId(generatedKey);
                            }
                        );

        }


        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, khSpend);

        final UUID generatedKeySpend = (UUID) khSpend.getKeys().get("id");
        spend.setId(generatedKeySpend);
        return spend;

    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"spend\" SET amount = ?, category_id = ?, currency = ?, description = ?, spend_date = ?, username = ? " +
                            "WHERE id = ?"
            );
            ps.setDouble(1, spend.getAmount());
            ps.setObject(2, spend.getCategory().getId());
            ps.setString(3, spend.getCurrency().name());
            ps.setString(4, spend.getDescription());
            ps.setDate(5, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(6, spend.getUsername());
            ps.setObject(7, spend.getId());
            return ps;
        });

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"category\" SET archived = ?, name = ?, username = ? " +
                            "WHERE id = ?"
            );
            ps.setBoolean(1, spend.getCategory().isArchived());
            ps.setString(2, spend.getCategory().getName());
            ps.setString(3, spend.getCategory().getUsername());
            ps.setObject(4, spend.getCategory().getId());
            return ps;
        });

        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (username, name, archived) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, keyHolder);

        UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        if (generatedKey == null) {
            throw new RuntimeException("Can't retrieve generated id for category");
        }

        category.setId(generatedKey);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                                SELECT * from category WHERE id = ?""",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

        List<CategoryEntity> list = jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ? AND name = ?",
                (rs, rowNum) -> {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId((UUID) rs.getObject("id"));
                    ce.setUsername(rs.getString("username"));
                    ce.setName(rs.getString("name"));
                    ce.setArchived(rs.getBoolean("archived"));
                    return ce;
                },
                username, categoryName
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                                SELECT
                                    spend.username,
                                    spend.spend_date,
                                    spend.currency,
                                    spend.amount,
                                    spend.description,
                                    spend.category_id,
                                    category.name,
                                    category.archived
                                FROM spend
                                JOIN category ON spend.category_id = category.id
                                WHERE spend.id = ?
                                """,
                        SpendEntityRowMapper.instance,
                        id
                )
        );

    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                                SELECT
                                    spend.username,
                                    spend.spend_date,
                                    spend.currency,
                                    spend.amount,
                                    spend.description,
                                    spend.category_id,
                                    category.name,
                                    category.archived
                                FROM spend
                                JOIN category ON spend.category_id = category.id
                                WHERE spend.username = ? and spend.description = ?
                                """,
                        SpendEntityRowMapper.instance,
                        username, description
                )
        );
    }

    @Override
    public void remove(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

        String sql = "DELETE FROM spend WHERE id = ?";

        jdbcTemplate.update(sql, spend.getId());

    }

    @Override
    public void removeCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

        String sql = "DELETE FROM category WHERE id = ?";

        jdbcTemplate.update(sql, category.getId());
    }
}
