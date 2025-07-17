package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.sql.Date;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.*;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpendSpringJdbc(SpendJson json) {
      SpendEntity spend = new SpendEntity();
      spend.setAmount(json.amount());
      spend.setCategory(CategoryEntity.fromJson(json.category()));
      spend.setCurrency(json.currency());
      spend.setDescription(json.description());
      spend.setSpendDate(new Date(json.spendDate().getTime()));
      spend.setUsername(json.username());

      return SpendJson.fromEntity(
          new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
              .create(spend)
      );
    }

    public SpendJson createSpend(SpendJson spend) {
        return transaction(
                connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        Optional<CategoryEntity> categoryDb = new CategoryDaoJdbc(connection).findByUsernameAndCategoryName(spendEntity.getCategory().getUsername(),
                                spendEntity.getCategory().getName());
                        spendEntity.setCategory(categoryDb.orElseGet(
                                () -> new CategoryDaoJdbc(connection).create(spendEntity.getCategory())
                        ));
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(
                connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    new SpendDaoJdbc(connection).deleteSpend(spendEntity);
                }, CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).create(categoryEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String name, String username) {
        return transaction(
                connection -> {
                    Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connection)
                            .findByUsernameAndCategoryName(name, username);
                    return categoryEntity.map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl()
        );

    }

    public void deleteCategory(CategoryJson category) {
        transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl()
        );
    }
}
