package guru.qa.niffler.data.mapper.userdata;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.MapRowMapper;
import guru.qa.niffler.model.CurrencyValues;

import java.util.Map;
import java.util.UUID;

public class UserdataUserEntityMapRowMapper<T> implements MapRowMapper<UdUserEntity> {

  public static final UserdataUserEntityMapRowMapper instance = new UserdataUserEntityMapRowMapper();

  private UserdataUserEntityMapRowMapper(){
  }

  @Override
  public UdUserEntity mapRow(Map<String, Object> row) {
    UdUserEntity ue = new UdUserEntity();
    ue.setId((UUID) row.get("id"));
    ue.setUsername((String) row.get("username"));
    ue.setCurrency((CurrencyValues) row.get("currency"));
    ue.setFirstname((String) row.get("firstname"));
    ue.setSurname((String) row.get("surname"));
    ue.setFullname((String) row.get("full_name"));
    ue.setPhoto((byte[]) row.get("photo"));
    ue.setPhotoSmall((byte[]) row.get("photo_small"));
    return ue;
  }
}
