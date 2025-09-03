package guru.qa.niffler.data.dao;

import com.github.javafaker.Bool;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    List<SpendEntity> findAllByUsername(String username);

    Boolean deleteSpend(UUID id);

    List<SpendEntity> findAll();
}
