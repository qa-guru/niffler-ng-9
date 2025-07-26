package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(AuthUserEntity.class, id)
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("select u from UserEntity u where u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return entityManager.createQuery("select u from AuthUserEntity", AuthUserEntity.class)
                .getResultList();
    }

    @Override
    public void delete(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}
