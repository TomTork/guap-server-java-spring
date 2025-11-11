package ru.anotherworld.server.db.dao;

import ru.anotherworld.server.db.model.WaterSupplyPE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Описывает интерфейс для доступа к базе данных потребления водоснабжения
 */
public interface WaterSupplyRepository extends CrudRepository<WaterSupplyPE, Integer> {

    List<WaterSupplyPE> findByActive(Boolean active);

    List<WaterSupplyPE> findByDebtGreaterThan(Float debt);

    List<WaterSupplyPE> findAll();

    Optional<WaterSupplyPE> findByApartmentId(Integer apartmentId);
}
