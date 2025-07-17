package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.sql.Date;
import java.util.Optional;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.spendJdbcUrl()
  );

  public SpendJson createSpendSpringJdbc(SpendJson json) {
    SpendEntity spend = new SpendEntity();
    spend.setAmount(json.amount());
    spend.setCategory(CategoryEntity.fromJson(json.category()));
    spend.setCurrency(json.currency());
    spend.setDescription(json.description());
    spend.setSpendDate(new Date(json.spendDate().getTime()));
    spend.setUsername(json.username());

    return SpendJson.fromEntity(spendDao.create(spend));
  }

  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate.execute(
        () -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            Optional<CategoryEntity> categoryDb = categoryDao.findByUsernameAndCategoryName(spendEntity.getCategory().getUsername(),
                spendEntity.getCategory().getName());
            spendEntity.setCategory(categoryDb.orElseGet(
                () -> categoryDao.create(spendEntity.getCategory())
            ));
          }
          return SpendJson.fromEntity(
              spendDao.create(spendEntity)
          );
        }
    );
  }

  public void deleteSpend(SpendJson spend) {
    jdbcTxTemplate.execute(
        () -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          spendDao.deleteSpend(spendEntity);
          return null;
        }
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    return jdbcTxTemplate.execute(
        () -> {
          CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
          return CategoryJson.fromEntity(
              categoryDao.create(categoryEntity));
        }
    );
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String name, String username) {
    return jdbcTxTemplate.execute(
        () -> {
          Optional<CategoryEntity> categoryEntity = categoryDao
              .findByUsernameAndCategoryName(name, username);
          return categoryEntity.map(CategoryJson::fromEntity);
        }
    );

  }

  public void deleteCategory(CategoryJson category) {
    jdbcTxTemplate.execute(
        () -> {
          CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
          categoryDao.deleteCategory(categoryEntity);
          return null;
        }
    );
  }
}
