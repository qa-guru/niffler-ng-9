package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

public interface SpendClient {
    public SpendJson createSpend(SpendJson spend);
    SpendJson update(SpendJson spend);
    CategoryJson createCategory(CategoryJson category);
    Optional<CategoryJson> findCategoryById(UUID id);
    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName);
    Optional<SpendJson> findById(UUID id);
    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);
    void removeCategory(CategoryJson category);

    void remove(SpendJson spend);
}
