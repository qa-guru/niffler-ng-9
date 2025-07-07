package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
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

  public void deleteSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    spendDao.deleteSpend(spendEntity);
  }

  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(
            categoryDao.create(categoryEntity)
    );
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String name, String username) {
    Optional<CategoryEntity> categoryEntity = categoryDao.findByUsernameAndCategoryName(name, username);
    return categoryEntity.map(CategoryJson::fromEntity);
  }

  public void deleteCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    categoryDao.deleteCategory(categoryEntity);
  }
}
