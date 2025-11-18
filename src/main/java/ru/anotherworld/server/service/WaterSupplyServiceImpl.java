package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.WaterSupplyRepository;
import ru.anotherworld.server.db.dao.ApartmentRepository;
import ru.anotherworld.server.db.model.WaterSupplyPE;
import ru.anotherworld.server.handler.EventSender;
import ru.anotherworld.server.handler.event.WaterSupplyEvent;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterSupplyServiceImpl implements WaterSupplyService {
    public boolean isSend = true;
    private final EventSender eventSender;

    private final WaterSupplyRepository waterSupplyRepository;
    private final ApartmentRepository apartmentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<WaterSupplyDTO> listAll() {
        return waterSupplyRepository.findAll().stream()
                .map(waterSupplyPE -> objectMapper.convertValue(waterSupplyPE, WaterSupplyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer apartmentId) {
        WaterSupplyEvent event = new WaterSupplyEvent(apartmentId, 0L, 0L, 0.0f, false);
        try {
            if (isSend) {
                eventSender.create(event, "delete");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send update event", e);
        }
        waterSupplyRepository.deleteById(apartmentId);
    }

    @Override
    public WaterSupplyDTO add(Long cold, Long hot, Float debt, Boolean active, Integer apartmentId) {
        Optional<WaterSupplyPE> existingWaterSupply = waterSupplyRepository.findByApartmentId(apartmentId);

        WaterSupplyPE waterSupply;
        if (existingWaterSupply.isPresent()) {
            waterSupply = existingWaterSupply.get();
            waterSupply.setCold(cold);
            waterSupply.setHot(hot);
            waterSupply.setDebt(debt);
            waterSupply.setActive(active);
        } else {
            waterSupply = new WaterSupplyPE(cold, hot, debt, active);
            waterSupply.setId(apartmentId);
            waterSupply.setCold(cold);
            waterSupply.setHot(hot);
            waterSupply.setDebt(debt != null ? debt : 0.0f);
            waterSupply.setActive(active != null ? active : true);
        }
        WaterSupplyDTO result = objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
        try {
            if (isSend) {
                eventSender.create(result, "create");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send create event", e);
        }
        return result;
    }

    @Override
    public WaterSupplyDTO update(Integer id, Long cold, Long hot, Float debt, Boolean active) {
        WaterSupplyPE waterSupply = waterSupplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Water supply record not found for id: " + id));

        waterSupply.setCold(cold);
        waterSupply.setHot(hot);
        waterSupply.setDebt(debt);
        waterSupply.setActive(active);

        WaterSupplyDTO result = objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
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
    public WaterSupplyDTO findByApartmentId(Integer apartmentId) {
        return waterSupplyRepository.findByApartmentId(apartmentId)
                .map(waterSupply -> objectMapper.convertValue(waterSupply, WaterSupplyDTO.class))
                .orElse(null);
    }

    @Override
    public WaterSupplyDTO findById(Integer id) {
        return waterSupplyRepository.findById(id)
                .map(waterSupplyPE -> objectMapper.convertValue(waterSupplyPE, WaterSupplyDTO.class))
                .orElse(null);
    }

    @Override
    public List<WaterSupplyDTO> findByActive(Boolean active) {
        return waterSupplyRepository.findByActive(active).stream()
                .map(waterSupplyPE -> objectMapper.convertValue(waterSupplyPE, WaterSupplyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<WaterSupplyDTO> findWithDebt() {
        return waterSupplyRepository.findByDebtGreaterThan(0.0f).stream()
                .map(waterSupplyPE -> objectMapper.convertValue(waterSupplyPE, WaterSupplyDTO.class))
                .collect(Collectors.toList());
    }
}
