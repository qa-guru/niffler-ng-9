package guru.qa.niffler.model.userdata;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.util.UUID;

public record UdUserJson(
    UUID id,
    String username,
    CurrencyValues currency,
    String fullname,
    String firstname,
    String surname,
    byte[] photo,
    byte[] photoSmall) {

  public static UdUserJson fromEntity(UdUserEntity entity) {
    return new UdUserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getCurrency(),
        entity.getFullname(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getPhoto(),
        entity.getPhotoSmall()
    );
  }
}
