package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Optional;
import java.util.UUID;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    SpendJson update(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name);

    Optional<SpendJson> findById(UUID id);

    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);

    void delete(SpendJson spend);

    void deleteCategory(CategoryJson category);
}
