package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.jdbc.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {

  private static final Config CFG = Config.getInstance();

  private final UserdataUserRepository udUserRepository = new UserdataUserRepositoryJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.userdataJdbcUrl()
  );

  public Optional<UdUserEntity> findById(UUID id) {
    return jdbcTxTemplate.execute(() -> {
          return udUserRepository.findById(id);
        }
    );
  }

  public Optional<UdUserEntity> findByUsername(String username) {
    return jdbcTxTemplate.execute(() -> {
          return udUserRepository.findByUsername(username);
        }
    );
  }

  public List<UdUserEntity> findAll() {
    return jdbcTxTemplate.execute(() -> {
          return udUserRepository.findAll();
        }
    );
  }

  public void createIncomeFriendshipInvitation(UdUserEntity requester, UdUserEntity addressee) {
    udUserRepository.addIncomeInvitation(requester, addressee);
  }
}
