package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import java.util.List;

public interface FriendshipDao {
    void create(FriendshipEntity friendship);
    List<FriendshipEntity> findAll();
}
