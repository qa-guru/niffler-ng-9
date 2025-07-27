package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {
  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
  KeyHolder kh = new GeneratedKeyHolder();
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
      ps.setObject(6, spend.getId());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    spend.setId(generatedKey);
    return spend;
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
            "SELECT spend.username, spend.spend_date, spend.currency, spend.amount, spend.description, spend.category_id, " +
                    "category.name, category.archived" +
                    "FROM spend " +
                    "WHERE id = ? " +
                    "JOIN category on spend.category_id = category.id",
                    SpendEntityRowMapper.instance,
                    id
            ));
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return new ArrayList<>(
            jdbcTemplate.query(
            "SELECT spend.username, spend.spend_date, spend.currency, spend.amount, spend.description, spend.category_id " +
                    "category.name, category.archived" +
                    "FROM spend " +
                    "WHERE spend.username = ? " +
                    "JOIN category on spend.category_id = category.id",
                    SpendEntityRowMapper.instance,
                    username
            ));
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
            "DELETE FROM spend WHERE id = ?",
            spend.getId()
    );
  }

  @Override
  public List<SpendEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return new ArrayList<>(
            jdbcTemplate.query(
                    "SELECT * from spend",
                    SpendEntityRowMapper.instance));
  }
}
