package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendshipEntityRowMapper implements RowMapper<FriendshipEntity> {

  public static final FriendshipEntityRowMapper instance = new FriendshipEntityRowMapper();

  private FriendshipEntityRowMapper() {
  }

  @Override
  public FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    FriendshipEntity result = new FriendshipEntity();
    result.getRequester().setId(rs.getObject("requester", UUID.class));
    result.getAddressee().setId(rs.getObject("addressee", UUID.class));
    result.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
    result.setCreatedDate(new java.util.Date(rs.getDate("created_date").getTime()));
    return result;
  }
}
