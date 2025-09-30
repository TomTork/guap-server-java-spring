/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.ElectricityDTO;
import java.util.List;

public interface ElectricityService {

    List<ElectricityDTO> listAll();

    void delete(Integer apartmentId);

    ElectricityDTO add(Long day, Long night, Float debt, Boolean active, Integer apartmentId);

    ElectricityDTO update(Integer apartmentId, Long day, Long night, Float debt, Boolean active);

    ElectricityDTO findByApartmentId(Integer apartmentId);

    List<ElectricityDTO> findByActive(Boolean active);

    List<ElectricityDTO> findWithDebt();
}
