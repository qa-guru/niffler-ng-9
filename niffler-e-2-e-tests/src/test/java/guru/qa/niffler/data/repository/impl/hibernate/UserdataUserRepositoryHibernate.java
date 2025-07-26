package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.userdataJdbcUrl());

    @Override
    public UdUserEntity create(UdUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<UdUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(UdUserEntity.class, id)
        );
    }

    @Override
    public List<UdUserEntity> findAll() {
        return entityManager.createQuery("select u from UdUserEntity u", UdUserEntity.class)
                .getResultList();
    }

    @Override
    public Optional<UdUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("select u from UdUserEntity u where u.username =: username", UdUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UdUserEntity update(UdUserEntity user) {
        entityManager.joinTransaction();
        return entityManager.merge(user);
    }

    @Override
    public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
        entityManager.joinTransaction();
        addressee.addFriends(FriendshipStatus.PENDING, requester);
    }

    @Override
    public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }

    @Override
    public void delete(UdUserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}