package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Connection;


public class CategoryDbClient {

  private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

  public CategoryJson createCategory(CategoryJson category) {
      return jdbcTxTemplate.execute(() -> {
              CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
              return CategoryJson.fromEntity(
                      categoryDao.create(categoryEntity)
              );
            },
            Connection.TRANSACTION_SERIALIZABLE
    );
  }

}
