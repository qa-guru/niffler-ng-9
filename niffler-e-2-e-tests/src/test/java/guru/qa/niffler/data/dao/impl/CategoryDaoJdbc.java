package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Statement.*;

public class CategoryDaoJdbc implements CategoryDao {

  private final Connection connection;

  public CategoryDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO category (username, name, archived) " +
            "VALUES (?, ?, ?)",
        RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      category.setId(generatedKey);
      return category;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM category WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("id", UUID.class));
          ce.setUsername(rs.getString("username"));
          ce.setName(rs.getString("name"));
          ce.setArchived(rs.getBoolean("archived"));
          return Optional.of(ce);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM category WHERE username = ? AND name = ?"
    )) {
      ps.setString(1, username);
      ps.setString(2, categoryName);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("id", UUID.class));
          ce.setUsername(rs.getString("username"));
          ce.setName(rs.getString("name"));
          ce.setArchived(rs.getBoolean("archived"));
          return Optional.of(ce);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM category WHERE username = ?"
    )) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        List<CategoryEntity> categoryList = new ArrayList<>();
        if (rs.next()) {
          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("id", UUID.class));
          ce.setUsername(rs.getString("username"));
          ce.setName(rs.getString("name"));
          ce.setArchived(rs.getBoolean("archived"));
          categoryList.add(ce);
        }
        return categoryList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCategory(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM category WHERE id = ?"
    )) {
      ps.setObject(1, category.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
