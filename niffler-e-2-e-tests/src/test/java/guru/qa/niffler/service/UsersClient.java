package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {
    UdUserJson create(UdUserJson user);

    Optional<UdUserJson> findById(UUID id);

    List<UdUserJson> findAll();

    Optional<UdUserJson> findByUsername(String username);

    UdUserJson update(UdUserJson user);

    void sendInvitation(UdUserJson requester, UdUserJson addressee);

    void addFriend(UdUserJson requester, UdUserJson addressee);

    void delete(UdUserJson user);
}
