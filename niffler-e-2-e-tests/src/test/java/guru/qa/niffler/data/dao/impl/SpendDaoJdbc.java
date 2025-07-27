package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();


  @Override
  public SpendEntity create(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
            "VALUES ( ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {

      ps.setString(1, spend.getUsername());
      ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getId());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
            "SELECT spend.username, spend.spend_date, spend.currency, spend.amount, spend.description, spend.category_id, " +
                    "category.name, category.archived" +
                    "FROM spend " +
                    "WHERE id = ? " +
                    "JOIN category on spend.category_id = category.id"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));

          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("category_id", UUID.class));
          ce.setUsername(rs.getString("username"));
          ce.setName(rs.getString("name"));
          ce.setArchived(rs.getBoolean("archived"));

          se.setCategory(ce);
          return Optional.of(se);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
            "SELECT spend.username, spend.spend_date, spend.currency, spend.amount, spend.description, spend.category_id " +
                    "category.name, category.archived" +
                    "FROM spend " +
                    "WHERE spend.username = ? " +
                    "JOIN category on spend.category_id = category.id"
    )) {
      ps.setObject(1, username);
      ps.execute();
      List<SpendEntity> listResult = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));

          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("category_id", UUID.class));
          ce.setUsername(rs.getString("username"));
          ce.setName(rs.getString("name"));
          ce.setArchived(rs.getBoolean("archived"));

          se.setCategory(ce);
          listResult.add(se);
        }
        return listResult;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
            "DELETE FROM spend WHERE id = ?"
    )) {
      ps.setObject(1, spend.getId());

      int deletedRows = ps.executeUpdate();
      System.out.printf("Deleted %d rows%n", deletedRows);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  private SpendEntity getSpendEntityFromResultSet(ResultSet rs) throws SQLException {
    SpendEntity result = new SpendEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setAmount(rs.getDouble("amount"));
    result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    result.setDescription(rs.getString("description"));
    result.setSpendDate(rs.getDate("spend_date"));
    result.setUsername(rs.getString("username"));

    CategoryEntity category = new CategoryEntity();
    category.setId(rs.getObject("category_id", UUID.class));

    return result;
  }

  @Override
  public List<SpendEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
            "SELECT * from category")){
      ps.execute();
      List<SpendEntity> resultList = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity ce = getSpendEntityFromResultSet(rs);
          resultList.add(ce);
        }
        return resultList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
