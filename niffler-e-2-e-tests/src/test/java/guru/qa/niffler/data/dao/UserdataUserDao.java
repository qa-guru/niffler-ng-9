package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {
  UdUserEntity create(UdUserEntity user);

  Optional<UdUserEntity> findById(UUID id);

  List<UdUserEntity> findAll();

  Optional<UdUserEntity> findByUsername(String username);

  void delete(UdUserEntity user);
}
