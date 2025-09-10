package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;


public class SpendHibernateClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.spendJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            return SpendJson.fromEntity(
                    spendRepository.create(spendEntity)
            );
        });

        }

    @Override
    public void remove(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.remove(spendEntity);
            return null;
        });
    }

}
