package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.auth.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson json) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(json.id());
        userEntity.setUsername(json.username());
        userEntity.setCurrency(json.currency());
        userEntity.setFirstname(json.firstname());
        userEntity.setSurname(json.surname());
        userEntity.setFullname(json.fullname());
        userEntity.setPhoto(json.photo().getBytes());
        userEntity.setPhotoSmall(json.photoSmall().getBytes());
        return userEntity;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", currency=" + currency +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", fullname='" + fullname + '\'' +
                ", photo=" + Arrays.toString(photo) +
                ", photoSmall=" + Arrays.toString(photoSmall) +
                '}';
    }
}
