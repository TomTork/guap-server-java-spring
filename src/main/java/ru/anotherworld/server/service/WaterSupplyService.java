/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import java.util.List;

public interface WaterSupplyService {

    List<WaterSupplyDTO> listAll();

    void delete(Integer apartmentId);

    WaterSupplyDTO add(Long cold, Long hot, Float debt, Boolean active, Integer apartmentId);

    WaterSupplyDTO update(Integer apartmentId, Long cold, Long hot, Float debt, Boolean active);

    WaterSupplyDTO findByApartmentId(Integer apartmentId);

    List<WaterSupplyDTO> findByActive(Boolean active);

    List<WaterSupplyDTO> findWithDebt();
}
