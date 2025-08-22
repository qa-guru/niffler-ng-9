package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.CategoryDAO;
import guru.qa.niffler.data.SpendDAO;
import guru.qa.niffler.data.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.impl.SpendDAOJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  private final CategoryDAO categoryDAO = new CategoryDAOJdbc();
  private final SpendDAO spendDAO = new SpendDAOJdbc();

  @Test
  void spendingDescriptionShouldBeChangedFromTable() throws SQLException {
    CategoryJson createdCategory = categoryDAO.createCategory(
        new CategoryJson(
            null,
            "Обучение",
            "duck",
            false
        )
    );

    SpendJson createdSpend = spendDAO.createSpend(
        new SpendJson(
            null,
            new Date(),
            createdCategory,
            CurrencyValues.RUB,
            89900.00,
            "Обучение Niffler 2.0, 10 поток",
            "duck"
        )
    );

    final String newDescription = "Fun with niffler!";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage("duck", "12345")
        .submit()
        .checkThatPageLoaded()
        .editSpending(createdSpend.description())
        .setNewSpendingDescription(newDescription)
        .save()
        .checkThatTableContainsSpending(newDescription);
  }
}
