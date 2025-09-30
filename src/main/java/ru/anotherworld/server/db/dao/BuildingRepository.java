package ru.anotherworld.server.db.dao;

import ru.anotherworld.server.db.model.BuildingPE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Описывает интерфейс для доступа к базе данных зданий
 */
public interface BuildingRepository extends CrudRepository<BuildingPE, Integer> {

    Optional<BuildingPE> findByCode(String code);

    List<BuildingPE> findAll();
}
