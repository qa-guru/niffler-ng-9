package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();
  private static final String URL = CFG.authJdbcUrl();

  @Override
  public void create(AuthorityEntity... authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
    jdbcTemplate.batchUpdate(
        "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority[i].getUser().getId());
            ps.setString(2, authority[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.length;
          }
        }
    );
  }

  @Override
  public List<AuthorityEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
    return jdbcTemplate.query(
        "SELECT * FROM \"authority\"",
        AuthorityEntityRowMapper.instance
    );
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
    return jdbcTemplate.query(
        "SELECT * FROM authority where user_id = ?",
        AuthorityEntityRowMapper.instance,
        userId
    );
  }
}
