package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      //first try to find category_id in category table
      Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(spend.username(), spend.category().name());
      categoryEntity.ifPresentOrElse(entity -> spendEntity.setCategory(entity),
              () -> {CategoryEntity entity = categoryDao.create(spendEntity.getCategory());
                    spendEntity.setCategory(entity);});

    }
    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }
}
