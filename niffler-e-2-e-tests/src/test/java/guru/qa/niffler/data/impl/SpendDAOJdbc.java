package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.CategoryDAO;
import guru.qa.niffler.data.SpendDAO;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

public class SpendDAOJdbc implements SpendDAO {
  @Override
  public SpendJson createSpend(SpendJson spend) throws SQLException {
    try (Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/niffler-spend",
        "postgres",
        "secret"
    );
         PreparedStatement ps = connection.prepareStatement(
             """
                 INSERT INTO "spend" (amount, category_id, currency, description, spend_date, username) VALUES (?, ?, ?, ?, ?, ?)
                 """,
             Statement.RETURN_GENERATED_KEYS
         )) {
      ps.setDouble(1, spend.amount());
      ps.setObject(2, spend.category().id());
      ps.setString(3, spend.currency().name());
      ps.setString(4, spend.description());
      ps.setDate(5, new java.sql.Date(spend.spendDate().getTime()));
      ps.setString(6, spend.username());
      ps.executeUpdate();

      try (ResultSet idRs = ps.getGeneratedKeys()) {
        if (idRs.next()) {
          return new SpendJson(
              idRs.getObject(1, UUID.class),
              spend.spendDate(),
              spend.category(),
              spend.currency(),
              spend.amount(),
              spend.description(),
              spend.username()
          );
        } else {
          throw new IllegalStateException("ID not found!");
        }
      }
    }
  }
}
