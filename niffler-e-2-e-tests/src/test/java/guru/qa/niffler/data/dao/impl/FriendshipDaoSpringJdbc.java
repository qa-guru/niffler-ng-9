package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.mapper.FriendshipEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class FriendshipDaoSpringJdbc implements FriendshipDao {

    private static final Config CFG = Config.getInstance();

  @Override
  public void create(FriendshipEntity friendship) {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
      jdbcTemplate.update(
              "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                      "VALUES (?, ?, ? ,?)",
              friendship.getRequester().getId(),
              friendship.getAddressee().getId(),
              friendship.getStatus().name(),
              new java.sql.Date(friendship.getCreatedDate().getTime())
      );
  }

    @Override
    public List<FriendshipEntity> findAll() {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
      return new ArrayList<>(
              jdbcTemplate.query(
                      "SELECT * from friendship",
                      FriendshipEntityRowMapper.instance));
    }
}
