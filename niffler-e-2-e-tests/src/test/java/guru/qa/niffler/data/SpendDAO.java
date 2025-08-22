package guru.qa.niffler.data;

import guru.qa.niffler.model.SpendJson;

import java.sql.SQLException;
import java.util.UUID;

public interface SpendDAO {

  SpendJson createSpend(SpendJson spend) throws SQLException;
}
