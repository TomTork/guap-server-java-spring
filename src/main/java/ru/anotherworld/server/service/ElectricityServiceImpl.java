package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.ElectricityRepository;
import ru.anotherworld.server.db.model.ElectricityPE;
import ru.anotherworld.server.handler.EventSender;
import ru.anotherworld.server.handler.event.ElectricityEvent;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectricityServiceImpl implements ElectricityService {
    public boolean isSend = true;
    private final EventSender eventSender;

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
        ElectricityEvent event = new ElectricityEvent(apartmentId, 0, 0, 0, false);
        try {
            if (isSend) {
                eventSender.create(event, "delete");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send delete event", e);
        }
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
        ElectricityDTO result = convertToElectricityDTO(electricityRepository.save(electricity));
        try {
            if (isSend) {
                eventSender.create(result, "create");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send update event", e);
        }
        return result;
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
        ElectricityDTO result = objectMapper.convertValue(electricityRepository.save(electricity), ElectricityDTO.class);
        try {
            if (isSend) {
                eventSender.create(result, "update");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send update event", e);
        }
        return result;
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
