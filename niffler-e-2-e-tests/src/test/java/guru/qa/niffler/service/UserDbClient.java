package guru.qa.niffler.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

public class UserDbClient {

    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    public UserEntity createUser(UserJson userJson) {
        UserEntity userEntity = UserEntity.fromJson(userJson);
        return userdataUserDao.createUser(userEntity);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    public Optional<UserEntity> findByUserName(String username) {
        return userdataUserDao.findByUserName(username);
    }

    public Boolean delete(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return userdataUserDao.delete(userEntity);
    }
}
