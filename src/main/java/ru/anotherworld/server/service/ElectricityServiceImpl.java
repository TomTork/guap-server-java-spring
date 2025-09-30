/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.ElectricityRepository;
import ru.anotherworld.server.db.dao.ApartmentRepository;
import ru.anotherworld.server.db.model.ElectricityPE;
import ru.anotherworld.server.db.model.ApartmentPE;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectricityServiceImpl implements ElectricityService {

    private final ElectricityRepository electricityRepository;
    private final ApartmentRepository apartmentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<ElectricityDTO> listAll() {
        return electricityRepository.findAll().stream()
                .map(electricityPE -> objectMapper.convertValue(electricityPE, ElectricityDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer apartmentId) {
        electricityRepository.deleteById(apartmentId);
    }

    @Override
    public ElectricityDTO add(Long day, Long night, Float debt, Boolean active, Integer apartmentId) {
        ApartmentPE apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartment not found with id: " + apartmentId));

        ElectricityPE electricity = new ElectricityPE(day, night, debt, active, apartment);
        return objectMapper.convertValue(electricityRepository.save(electricity), ElectricityDTO.class);
    }

    @Override
    public ElectricityDTO update(Integer apartmentId, Long day, Long night, Float debt, Boolean active) {
        ElectricityPE electricity = electricityRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Electricity record not found for apartment id: " + apartmentId));

        electricity.setDay(day);
        electricity.setNight(night);
        electricity.setDebt(debt);
        electricity.setActive(active);

        return objectMapper.convertValue(electricityRepository.save(electricity), ElectricityDTO.class);
    }

    @Override
    public ElectricityDTO findByApartmentId(Integer apartmentId) {
        var electricityPE = electricityRepository.findById(apartmentId);
        return electricityPE.map(electricity -> objectMapper.convertValue(electricity, ElectricityDTO.class)).orElse(null);
    }

    @Override
    public List<ElectricityDTO> findByActive(Boolean active) {
        return electricityRepository.findByActive(active).stream()
                .map(electricityPE -> objectMapper.convertValue(electricityPE, ElectricityDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ElectricityDTO> findWithDebt() {
        return electricityRepository.findByDebtGreaterThan(0.0f).stream()
                .map(electricityPE -> objectMapper.convertValue(electricityPE, ElectricityDTO.class))
                .collect(Collectors.toList());
    }
}
