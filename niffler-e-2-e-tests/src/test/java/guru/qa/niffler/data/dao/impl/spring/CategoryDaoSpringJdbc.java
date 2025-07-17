package guru.qa.niffler.data.dao.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.spend.CategoryEntityMapRowMapper;
import guru.qa.niffler.data.mapper.spend.CategoryEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryDaoSpringJdbc implements CategoryDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public CategoryEntity create(CategoryEntity category) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO \"category\" (archived, name, username) VALUES (?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setBoolean(1, category.isArchived());
          ps.setString(2, category.getName());
          ps.setString(3, category.getUsername());
          return ps;
        }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    category.setId(generatedKey);
    return category;
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"category\" WHERE id = ?",
            CategoryEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"category\" WHERE username = ? AND name = ?",
            CategoryEntityRowMapper.instance,
            username,
            categoryName
        )
    );
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.queryForList(
            "SELECT * FROM \"category\" WHERE username = ?",
            username
        ).stream()
        .map(CategoryEntityMapRowMapper.instance::mapRow)
        .collect(Collectors.toList());
  }

  @Override
  public List<CategoryEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.queryForList(
            "SELECT * FROM \"category\""
        ).stream()
        .map(CategoryEntityMapRowMapper.instance::mapRow)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteCategory(CategoryEntity category) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM \"category\" WHERE id = ?",
        category.getId()
    );
  }
}
