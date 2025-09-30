/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.ApartmentRepository;
import ru.anotherworld.server.db.dao.BuildingRepository;
import ru.anotherworld.server.db.model.ApartmentPE;
import ru.anotherworld.server.db.model.BuildingPE;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<ApartmentDTO> listAll() {
        return apartmentRepository.findAll().stream()
                .map(apartmentPE -> objectMapper.convertValue(apartmentPE, ApartmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public ApartmentDTO add(String number, Float totalSquare, Float livingSquare, Integer roomsAmount, Integer floor, Integer buildingId) {
        BuildingPE building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        ApartmentPE apartment = new ApartmentPE(number, totalSquare, livingSquare, roomsAmount, floor, building);
        return objectMapper.convertValue(apartmentRepository.save(apartment), ApartmentDTO.class);
    }

    @Override
    public ApartmentDTO findByNumber(String number) {
        var apartmentPE = apartmentRepository.findByNumber(number);
        return apartmentPE.map(apartment -> objectMapper.convertValue(apartment, ApartmentDTO.class)).orElse(null);
    }

    @Override
    public ApartmentDTO findById(Integer id) {
        var apartmentPE = apartmentRepository.findById(id);
        return apartmentPE.map(apartment -> objectMapper.convertValue(apartment, ApartmentDTO.class)).orElse(null);
    }

    @Override
    public List<ApartmentDTO> findByBuildingId(Integer buildingId) {
        BuildingPE building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        return apartmentRepository.findByBuilding(building).stream()
                .map(apartmentPE -> objectMapper.convertValue(apartmentPE, ApartmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentDTO> findByFloor(Integer floor) {
        return apartmentRepository.findByFloor(floor).stream()
                .map(apartmentPE -> objectMapper.convertValue(apartmentPE, ApartmentDTO.class))
                .collect(Collectors.toList());
    }
}
