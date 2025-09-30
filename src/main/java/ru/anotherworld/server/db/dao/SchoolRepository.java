package ru.anotherworld.server.db.dao;

import ru.anotherworld.server.db.model.SchoolPE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Описывает интерфейс для доступа к базе данных школ
 */
public interface SchoolRepository extends CrudRepository<SchoolPE, Integer> {

    public Optional<SchoolPE> findByNumber(Integer number);

    public List<SchoolPE> findAll();
}
