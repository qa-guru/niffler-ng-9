package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;


public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                  SpendEntity spendEntity = SpendEntity.fromJson(spend);
                  return SpendJson.fromEntity(
                          spendRepository.create(spendEntity)
                  );
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
  }

    @Override
    public SpendJson update(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    return SpendJson.fromEntity(
                            spendRepository.update(spendEntity)
                    );
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            spendRepository.createCategory(categoryEntity)
                    );
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return Optional.of(CategoryJson.fromEntity(spendRepository.findCategoryById(id).orElseThrow()));
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return Optional.of(CategoryJson.fromEntity(
                spendRepository.findCategoryByUsernameAndCategoryName(username, categoryName).orElseThrow()));
    }

    @Override
    public Optional<SpendJson> findById(UUID id) {
        return Optional.of(SpendJson.fromEntity(spendRepository.findById(id).orElseThrow()));
    }

    @Override
    public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
        return Optional.of(SpendJson.fromEntity(
                spendRepository.findByUsernameAndSpendDescription(username, description).orElseThrow()));
    }

    @Override
    public void removeCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    spendRepository.removeCategory(categoryEntity);
                    return null;
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    @Override
    public void remove(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    spendRepository.remove(spendEntity);
                    return null;
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }
}
