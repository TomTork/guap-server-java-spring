package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.ElectricityDTO;

import java.util.List;

public interface ElectricityService {

    List<ElectricityDTO> listAll();

    void delete(Integer apartmentId);

    ElectricityDTO add(Long day, Long night, Float debt, Boolean active, Integer apartmentId);

    ElectricityDTO update(Integer id, Long day, Long night, Float debt, Boolean active);

    ElectricityDTO findByApartmentId(Integer apartmentId);

    List<ElectricityDTO> findByActive(Boolean active);

    List<ElectricityDTO> findWithDebt();

    ElectricityDTO findById(Integer id);
}
