package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.auth.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUserName(String userName);

    Boolean delete(UserEntity user);

    List<UserEntity> findAll();
}
