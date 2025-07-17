package guru.qa.niffler.data.mapper.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.MapRowMapper;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

public class SpendEntityMapRowMapper implements MapRowMapper<SpendEntity> {

  public static final SpendEntityMapRowMapper instance = new SpendEntityMapRowMapper();

  private SpendEntityMapRowMapper() {
  }

  @Override
  public SpendEntity mapRow(Map row) {
    SpendEntity se = new SpendEntity();

    se.setId((UUID) row.get("id"));
    se.setAmount((Double) row.get("amount"));
    se.setCategory((CategoryEntity) row.get("category_id"));
    se.setCurrency((CurrencyValues) row.get("currency"));
    se.setDescription((String) row.get("description"));
    se.setSpendDate((Date) row.get("spend_date"));
    se.setUsername((String) row.get("username"));
    return se;
  }
}
