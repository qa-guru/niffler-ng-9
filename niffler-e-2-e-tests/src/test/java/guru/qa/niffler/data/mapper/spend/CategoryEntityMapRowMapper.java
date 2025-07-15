package guru.qa.niffler.data.mapper.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.MapRowMapper;

import java.util.Map;
import java.util.UUID;

public class CategoryEntityMapRowMapper implements MapRowMapper<CategoryEntity> {

  public static final CategoryEntityMapRowMapper instance = new CategoryEntityMapRowMapper();

  private CategoryEntityMapRowMapper() {
  }

  @Override
  public CategoryEntity mapRow(Map row) {
    CategoryEntity ce = new CategoryEntity();
    ce.setId((UUID) row.get("id"));
    ce.setName((String) row.get("name"));
    ce.setUsername((String) row.get("username"));
    ce.setArchived((Boolean) row.get("archived"));
    return ce;
  }
}
