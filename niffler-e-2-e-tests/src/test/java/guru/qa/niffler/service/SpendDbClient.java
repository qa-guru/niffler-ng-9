package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend, int transactionLevel) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                },
                transactionLevel
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id, int transactionLevel) {
//        return transaction(connection -> {
//                    Optional<SpendEntity> spend = new SpendDaoJdbc(connection)
//                            .findSpendById(id);
//                    return spend;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return Optional.empty();
    }
//
    public List<SpendEntity> findAllByUsername(String username, int transactionLevel) {
//        return transaction(connection -> {
//                    List<SpendEntity> spend = new SpendDaoJdbc(connection)
//                            .findAllByUsername(username);
//                    return spend;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return List.of();
    }

    public Boolean deleteSpend(UUID id, int transactionLevel) {
//        return transaction(connection -> {
//                    Boolean spend = new SpendDaoJdbc(connection)
//                            .deleteSpend(id);
//                    return spend;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return null;
    }

    public CategoryEntity createCategory(CategoryJson categoryJson, int transactionLevel) {
//        return transaction(connection -> {
//                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
//                    return new CategoryDaoJdbc(connection)
//                            .create(categoryEntity);
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return null;
    }

    public List<CategoryEntity> findAllByUserName(String username, int transactionLevel) {
//        return transaction(connection -> {
//                    List<CategoryEntity> categories = new CategoryDaoJdbc(connection)
//                            .findAllByUserName(username);
//                    return categories;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return List.of();
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName, int transactionLevel) {
//        return transaction(connection -> {
//                    Optional<CategoryEntity> categories = new CategoryDaoJdbc(connection)
//                            .findCategoryByUsernameAndCategoryName(username, categoryName);
//                    return categories;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return Optional.empty();
    }

    public Boolean updateCategory(CategoryJson category, int transactionLevel) {
//        return transaction(connection -> {
//                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
//                    return new CategoryDaoJdbc(connection)
//                            .update(categoryEntity);
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return null;
    }

    public Boolean deleteCategory(UUID id, int transactionLevel) {
//        return transaction(connection -> {
//                    Boolean categories = new CategoryDaoJdbc(connection)
//                            .delete(id);
//                    return categories;
//                },
//                CFG.spendJdbcUrl(),
//                transactionLevel
//        );
        return null;
    }
}