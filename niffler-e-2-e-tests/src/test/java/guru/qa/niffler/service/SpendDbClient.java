package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson json) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity se = SpendEntity.fromJson(json);
                    if (se.getCategory().getId() == null) {
                        Optional<CategoryEntity> categoryDb = spendRepository
                                .findCategoryByUsernameAndSpendName(se.getCategory().getUsername(),
                                        se.getCategory().getName());
                        se.setCategory(categoryDb.orElseGet(
                                () -> spendRepository.createCategory(se.getCategory())
                        ));
                    }
                    return SpendJson.fromEntity(spendRepository.createSpend(se));
                }
        );
    }

    public SpendJson updateSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity se = SpendEntity.fromJson(spend);
                    return SpendJson.fromEntity(spendRepository.update(se));
                }
        );
    }

    public CategoryJson createCategory(CategoryJson json) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity se = CategoryEntity.fromJson(json);
                    return CategoryJson.fromEntity(spendRepository.createCategory(se));
                }
        );
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        return spendRepository.findCategoryByUsernameAndSpendName(username, name)
                .map(CategoryJson::fromEntity);
    }

    public Optional<SpendJson> findById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
        return spendRepository.findByUsernameAndSpendDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    public void deleteSpend(SpendJson json) {
        jdbcTxTemplate.execute(() -> {
                    spendRepository.delete(SpendEntity.fromJson(json));
                    return null;
                }
        );
    }

    public void deleteCategory(CategoryJson json) {
        jdbcTxTemplate.execute(() -> {
                    spendRepository.deleteCategory(CategoryEntity.fromJson(json));
                    return null;
                }
        );
    }
}
