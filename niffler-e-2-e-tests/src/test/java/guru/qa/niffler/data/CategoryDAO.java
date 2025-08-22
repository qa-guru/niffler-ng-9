package guru.qa.niffler.data;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.SQLException;
import java.util.UUID;

public interface CategoryDAO {

  CategoryJson createCategory(CategoryJson category) throws SQLException;
}
