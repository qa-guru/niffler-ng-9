package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;


public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.authJdbcUrl())
      )
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  @Override
  public UserJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = authUserEntity(username, password);
          authUserRepository.create(authUser);
          return UserJson.fromEntity(
              userdataUserRepository.create(userEntity(username)),
              null
          );
        }
    );
  }

  @Override
  public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
    final List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity adressee = userdataUserRepository.create(userEntity(username));
          userdataUserRepository.addFriendshipRequest(adressee, targetEntity);
          result.add(UserJson.fromEntity(
              adressee,
              FriendshipStatus.INVITE_RECEIVED
          ));
              return null;
            }
        );
      }
    }
    return result;
  }

  @Override
  public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
    final List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity adressee = userdataUserRepository.create(userEntity(username));
          userdataUserRepository.addFriendshipRequest(targetEntity, adressee);
          result.add(UserJson.fromEntity(
              adressee,
              FriendshipStatus.INVITE_RECEIVED
          ));
              return null;
            }
        );
      }
    }
    return result;
  }

  @Override
  public List<UserJson> addFriend(UserJson targetUser, int count) {
    final List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity adressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.addFriend(targetEntity, adressee);
          result.add(UserJson.fromEntity(
              adressee,
              FriendshipStatus.FRIEND
          ));
              return null;
            }
        );
      }
    }
    return result;
  }

  private UserEntity userEntity(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
        Arrays.stream(Authority.values()).map(
            e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(authUser);
              ae.setAuthority(e);
              return ae;
            }
        ).toList()
    );
    return authUser;
  }
}
