package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthAuthorityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class AuthAuthorityRepositoryHibernate implements AuthAuthorityRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.authJdbcUrl());

    @Override
    public void create(AuthorityEntity... authorities) {
        entityManager.joinTransaction();
        for (AuthorityEntity authority : authorities) {
            entityManager.persist(authority);
        }
    }

    @Override
    public List<AuthorityEntity> findByUserId(UUID userId) {
        try {
            return entityManager.createQuery("select a from AuthorityEntity a where a.user_id =: userId", AuthorityEntity.class)
                            .setParameter("userId", userId)
                            .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        try {
            return entityManager.createQuery("select a from AuthorityEntity a", AuthorityEntity.class)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void delete(AuthorityEntity authority) {
        entityManager.joinTransaction();
        entityManager.remove(authority);
    }
}
