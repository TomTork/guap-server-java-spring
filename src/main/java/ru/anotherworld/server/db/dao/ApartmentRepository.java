package ru.anotherworld.server.db.dao;

import ru.anotherworld.server.db.model.ApartmentPE;
import ru.anotherworld.server.db.model.BuildingPE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Описывает интерфейс для доступа к базе данных квартир
 */
public interface ApartmentRepository extends CrudRepository<ApartmentPE, Integer> {

    Optional<ApartmentPE> findByNumber(String number);

    List<ApartmentPE> findByBuilding(BuildingPE building);

    List<ApartmentPE> findByFloor(Integer floor);

    List<ApartmentPE> findAll();
}
