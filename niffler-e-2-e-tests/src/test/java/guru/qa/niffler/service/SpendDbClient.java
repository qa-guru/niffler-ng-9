package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.spring.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spring.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Optional;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final CategoryDao categoryDaoSpring = new CategoryDaoSpringJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();
  private final SpendDao spendDaoSpring = new SpendDaoSpringJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.spendJdbcUrl()
  );

  public SpendJson createSpendSpringJdbc(SpendJson json) {
    return jdbcTxTemplate.execute(() -> {
          SpendEntity se = SpendEntity.fromJson(json);
          if (se.getCategory().getId() == null) {
            Optional<CategoryEntity> categoryDb = categoryDao
                .findByUsernameAndCategoryName(se.getCategory().getUsername(),
                    se.getCategory().getName());
            se.setCategory(categoryDb.orElseGet(
                () -> categoryDao.create(se.getCategory())
            ));
          }
          return SpendJson.fromEntity(spendDao.create(se));
        }
    );
  }

  public SpendJson createSpend(SpendJson json) {
    return jdbcTxTemplate.execute(() -> {
          SpendEntity se = SpendEntity.fromJson(json);
          if (se.getCategory().getId() == null) {
            Optional<CategoryEntity> categoryDb = categoryDao
                .findByUsernameAndCategoryName(se.getCategory().getUsername(),
                    se.getCategory().getName());
            se.setCategory(categoryDb.orElseGet(
                () -> categoryDao.create(se.getCategory())
            ));
          }
          return SpendJson.fromEntity(spendDao.create(se));
        }
    );
  }

  public void deleteSpendSpringJdbc(SpendJson json) {
    SpendEntity spend = SpendEntity.fromJson(json);
    spendDaoSpring.deleteSpend(spend);
  }

  public void deleteSpend(SpendJson json) {
    SpendEntity spendEntity = SpendEntity.fromJson(json);
    spendDao.deleteSpend(spendEntity);
  }

  public CategoryJson createCategorySpringJdbc(CategoryJson json) {
    CategoryEntity ce = CategoryEntity.fromJson(json);
    return CategoryJson.fromEntity(categoryDaoSpring.create(ce));
  }

  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(
        categoryDao.create(categoryEntity));
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryNameSpringJdbc(String name, String username) {
    Optional<CategoryEntity> ce = categoryDaoSpring.findByUsernameAndCategoryName(name, username);
    return ce.map(CategoryJson::fromEntity);
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String name, String username) {
    Optional<CategoryEntity> categoryEntity = categoryDao
        .findByUsernameAndCategoryName(name, username);
    return categoryEntity.map(CategoryJson::fromEntity);
  }

  public void deleteCategorySpringJdbc(CategoryJson json) {
    CategoryEntity ce = CategoryEntity.fromJson(json);
    categoryDaoSpring.deleteCategory(ce);
  }

  public void deleteCategory(CategoryJson json) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(json);
    categoryDao.deleteCategory(categoryEntity);
  }
}
