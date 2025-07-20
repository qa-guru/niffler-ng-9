package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
  AuthUserEntity createAuthUser(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  Optional<AuthUserEntity> findByUsername(String username);

  List<AuthUserEntity> findAll();

  void deleteUser(AuthUserEntity user);
}
