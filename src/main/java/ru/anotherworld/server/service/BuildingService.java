package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.BuildingDTO;
import java.util.List;

public interface BuildingService {

    List<BuildingDTO> listAll();

    List<BuildingDTO> listAllWithApartments();

    void delete(Integer id);

    BuildingDTO add(String name, String code);

    BuildingDTO update(Integer id, String name, String code);

    BuildingDTO findByCode(String code);

    BuildingDTO findById(Integer id);
}
