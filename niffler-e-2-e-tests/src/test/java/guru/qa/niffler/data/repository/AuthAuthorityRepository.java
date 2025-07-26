package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityRepository {
  void create(AuthorityEntity... authorities);

  List<AuthorityEntity> findByUserId(UUID userId);

  List<AuthorityEntity> findAll();

  void delete(AuthorityEntity authority);
}
