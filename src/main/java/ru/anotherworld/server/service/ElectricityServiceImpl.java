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
        ElectricityPE electricity = new ElectricityPE();
        electricity.setId(apartmentId);
        electricity.setDay(day);
        electricity.setNight(night);
        electricity.setDebt(debt);
        electricity.setActive(active);
        return convertToElectricityDTO(electricityRepository.save(electricity));
    }

    private ElectricityDTO convertToElectricityDTO(ElectricityPE electricityPE) {
        ElectricityDTO electricityDTO = new ElectricityDTO();
        electricityDTO.setId(electricityPE.getId());
        electricityDTO.setDay(electricityPE.getDay());
        electricityDTO.setNight(electricityPE.getNight());
        electricityDTO.setDebt(electricityPE.getDebt());
        electricityDTO.setActive(electricityPE.getActive());
        return electricityDTO;
    }

    @Override
    public ElectricityDTO update(Integer id, Long day, Long night, Float debt, Boolean active) {
        ElectricityPE electricity = new ElectricityPE();
        electricity.setId(id);
        electricity.setDay(day);
        electricity.setNight(night);
        electricity.setDebt(debt);
        electricity.setActive(active);
        return objectMapper.convertValue(electricityRepository.save(electricity), ElectricityDTO.class);
    }

    @Override
    public ElectricityDTO findByApartmentId(Integer apartmentId) {
        return electricityRepository.findByApartmentId(apartmentId)
                .map(electricity -> objectMapper.convertValue(electricity, ElectricityDTO.class))
                .orElse(null);
    }

    @Override
    public ElectricityDTO findById(Integer id) {
        return electricityRepository.findById(id)
                .map(electricity -> objectMapper.convertValue(electricity, ElectricityDTO.class))
                .orElse(null);
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
