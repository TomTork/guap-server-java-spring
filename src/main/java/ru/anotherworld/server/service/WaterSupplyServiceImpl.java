package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.WaterSupplyRepository;
import ru.anotherworld.server.db.dao.ApartmentRepository;
import ru.anotherworld.server.db.model.WaterSupplyPE;
import ru.anotherworld.server.db.model.ApartmentPE;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterSupplyServiceImpl implements WaterSupplyService {

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
        waterSupplyRepository.deleteById(apartmentId);
    }

    @Override
    public WaterSupplyDTO add(Long cold, Long hot, Float debt, Boolean active, Integer apartmentId) {
        Optional<WaterSupplyPE> existingWaterSupply = waterSupplyRepository.findByApartmentId(apartmentId);
        
        if (existingWaterSupply.isPresent()) {
            WaterSupplyPE waterSupply = existingWaterSupply.get();
            waterSupply.setCold(cold);
            waterSupply.setHot(hot);
            waterSupply.setDebt(debt);
            waterSupply.setActive(active);
            return objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
        } else {
            WaterSupplyPE waterSupply = new WaterSupplyPE(cold, hot, debt, active);
            waterSupply.setId(apartmentId);
            waterSupply.setCold(cold);
            waterSupply.setHot(hot);
            waterSupply.setDebt(debt != null ? debt : 0.0f);
            waterSupply.setActive(active != null ? active : true);
            return objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
        }
    }

    @Override
    public WaterSupplyDTO update(Integer id, Long cold, Long hot, Float debt, Boolean active) {
        WaterSupplyPE waterSupply = waterSupplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Water supply record not found for id: " + id));

        waterSupply.setCold(cold);
        waterSupply.setHot(hot);
        waterSupply.setDebt(debt);
        waterSupply.setActive(active);

        return objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
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
