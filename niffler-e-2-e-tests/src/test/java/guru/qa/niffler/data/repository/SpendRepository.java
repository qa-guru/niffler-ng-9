package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {
    SpendEntity createSpend(SpendEntity spend);

    SpendEntity update(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

    Optional<SpendEntity> findById(UUID id);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void delete(SpendEntity spend);

    void deleteCategory(CategoryEntity category);
}
