package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.sql.Connection;


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
