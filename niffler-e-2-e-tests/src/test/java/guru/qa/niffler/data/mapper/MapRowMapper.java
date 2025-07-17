package guru.qa.niffler.data.mapper;

import java.util.Map;

@FunctionalInterface
public interface MapRowMapper<T>{
  T mapRow(Map<String, Object> row);
}
