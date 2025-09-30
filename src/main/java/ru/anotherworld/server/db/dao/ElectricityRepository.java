package ru.anotherworld.server.db.dao;

import ru.anotherworld.server.db.model.ElectricityPE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Описывает интерфейс для доступа к базе данных потребления электроэнергии
 */
public interface ElectricityRepository extends CrudRepository<ElectricityPE, Integer> {

    List<ElectricityPE> findByActive(Boolean active);

    List<ElectricityPE> findByDebtGreaterThan(Float debt);

    List<ElectricityPE> findAll();
}
