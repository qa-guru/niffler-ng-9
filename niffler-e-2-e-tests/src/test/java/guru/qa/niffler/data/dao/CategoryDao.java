package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Boolean update(CategoryEntity category);

    Boolean delete(UUID id);

    List<CategoryEntity> findAllByUserName(String username);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryEntity> findAll();
}