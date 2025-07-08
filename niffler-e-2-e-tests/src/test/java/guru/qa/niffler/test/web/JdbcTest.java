package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Disabled
public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );

        System.out.println(spend);
    }

    @Test
    void checkCategoryUpdate(){
        SpendDbClient spendDbClient = new SpendDbClient();

       boolean rows =  spendDbClient.updateCategory(
                new CategoryJson(
                        UUID.fromString("3410a389-d9ab-4e71-a4e6-d14f0027b850"),
                        "bla 2",
                        "bla bla bla",
                        true
                )
        );
        Assertions.assertTrue(rows);
    }

    @Test
    void checkCategoryByUsername() {
        SpendDbClient spendDbClient = new SpendDbClient();

        List<CategoryEntity> rows =  spendDbClient.findAllByUserName("marina");
        for (CategoryEntity row : rows) {
            System.err.println(row.getId());
            System.err.println(row.getName());
            System.err.println(row.getUsername());
            System.err.println(row.isArchived());
        }
    }

    @Test
    void checkCategoryByUsernameAndCategoryName() {
        SpendDbClient spendDbClient = new SpendDbClient();

        Optional<CategoryEntity> rows =  spendDbClient.findCategoryByUsernameAndCategoryName("marina", "31241412412");
    }

    @Test
    void checkSpendsByUsername() {
        SpendDbClient spendDbClient = new SpendDbClient();

        List<SpendEntity> rows = spendDbClient.findAllByUsername("marina");
        System.err.println(rows.toString());
    }

    @Test
    void checkSpendById() {
        SpendDbClient spendDbClient = new SpendDbClient();

        Optional<SpendEntity> rows = spendDbClient.findSpendById(UUID.fromString("36b9a123-bec3-4d52-b38b-212e1c6e358f"));
        System.err.println(rows.toString());
    }

    @Test
    void checkFindUserByUsername() {
        UserDbClient userDbClient = new UserDbClient();

        Optional<UserEntity> rows = userDbClient.findByUserName("marina");
        System.err.println(rows.toString());
    }
}
