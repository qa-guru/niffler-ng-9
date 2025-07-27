package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.userdata.UdUserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository udUserRepository = new UserdataUserRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.userdataJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UdUserJson create(UdUserJson user) {
        UdUserEntity ue = UdUserEntity.fromJson(user);
        return xaTransactionTemplate.execute(() -> {
                    return UdUserJson.fromEntity(udUserRepository.create(ue));
                }
        );
    }

    public Optional<UdUserJson> findById(UUID id) {
        return udUserRepository.findById(id)
                .map(UdUserJson::fromEntity);
    }

    public List<UdUserEntity> findAll() {
        return udUserRepository.findAll();
    }

    public Optional<UdUserEntity> findByUsername(String username) {
        return udUserRepository.findByUsername(username);
    }

    public UdUserJson update(UdUserJson user) {
        UdUserEntity ue = UdUserEntity.fromJson(user);
        return jdbcTxTemplate.execute(() -> {
                    return UdUserJson.fromEntity(udUserRepository.update(ue));
                }
        );
    }

    public void sendInvitation(UdUserJson requester, UdUserJson addressee) {
        jdbcTxTemplate.execute(() -> {
                    UdUserEntity req = UdUserEntity.fromJson(requester);
                    UdUserEntity add = UdUserEntity.fromJson(addressee);
                    udUserRepository.sendInvitation(req, add);
                    return null;
                }
        );
    }

    public void addFriend(UdUserJson requester, UdUserJson addressee) {
        jdbcTxTemplate.execute(() -> {
                    UdUserEntity req = UdUserEntity.fromJson(requester);
                    UdUserEntity add = UdUserEntity.fromJson(addressee);
                    udUserRepository.addFriend(req, add);
                    return null;
                }
        );
    }
}
