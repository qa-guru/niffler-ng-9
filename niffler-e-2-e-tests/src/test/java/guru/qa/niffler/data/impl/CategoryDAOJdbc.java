package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.CategoryDAO;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class CategoryDAOJdbc implements CategoryDAO {
  @Override
  public CategoryJson createCategory(CategoryJson category) throws SQLException {
    try (Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/niffler-spend",
        "postgres",
        "secret"
    );
         PreparedStatement ps = connection.prepareStatement(
             """
                 INSERT INTO "category" (name, username, archived) VALUES (?, ?, ?)
                 """,
             Statement.RETURN_GENERATED_KEYS
         )) {
      ps.setString(1, category.name());
      ps.setString(2, category.username());
      ps.setBoolean(3, category.archived());
      ps.executeUpdate();

      try (ResultSet idRs = ps.getGeneratedKeys()) {
        if (idRs.next()) {
          return new CategoryJson(
              idRs.getObject(1, UUID.class),
              category.name(),
              category.username(),
              category.archived()
          );
        } else {
          throw new IllegalStateException("ID not found!");
        }
      }
    }
  }
}
