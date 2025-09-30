/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.ApartmentDTO;
import java.util.List;

public interface ApartmentService {

    List<ApartmentDTO> listAll();

    void delete(Integer id);

    ApartmentDTO add(String number, Float totalSquare, Float livingSquare, Integer roomsAmount, Integer floor, Integer buildingId);

    ApartmentDTO findByNumber(String number);

    ApartmentDTO findById(Integer id);

    List<ApartmentDTO> findByBuildingId(Integer buildingId);

    List<ApartmentDTO> findByFloor(Integer floor);
}
