package guru.qa.niffler.data.dao.impl.spring;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.spend.SpendEntityMapRowMapper;
import guru.qa.niffler.data.mapper.spend.SpendEntityRowMapper;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpendDaoSpringJdbc implements SpendDao {

  private DataSource dataSource;

  public SpendDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public SpendEntity create(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO spend (amount, category_id, currency, description, spend_date, username) VALUES (?, ?, ?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setDouble(1, spend.getAmount());
          ps.setObject(2, spend.getCategory().getId());
          ps.setObject(3, spend.getCurrency().name());
          ps.setString(4, spend.getDescription());
          ps.setDate(5, spend.getSpendDate());
          ps.setString(6, spend.getUsername());
          return ps;
        }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    spend.setId(generatedKey);
    return spend;
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"spend\" WHERE id = ?",
            SpendEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    return jdbcTemplate.queryForList(
            "SELECT * FROM \"spend\" WHERE username = ?",
            username
        ).stream()
        .map(SpendEntityMapRowMapper.instance::mapRow)
        .collect(Collectors.toList());
  }

  @Override
  public List<SpendEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    return jdbcTemplate.queryForList(
            "SELECT * FROM \"spend\""
        ).stream()
        .map(SpendEntityMapRowMapper.instance::mapRow)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM \"spend\" WHERE id = ?",
        spend.getId()
    );
  }
}
