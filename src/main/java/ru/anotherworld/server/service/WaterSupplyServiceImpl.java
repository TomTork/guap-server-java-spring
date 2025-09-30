/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
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
        ApartmentPE apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartment not found with id: " + apartmentId));

        WaterSupplyPE waterSupply = new WaterSupplyPE(cold, hot, debt, active, apartment);
        return objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
    }

    @Override
    public WaterSupplyDTO update(Integer apartmentId, Long cold, Long hot, Float debt, Boolean active) {
        WaterSupplyPE waterSupply = waterSupplyRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Water supply record not found for apartment id: " + apartmentId));

        waterSupply.setCold(cold);
        waterSupply.setHot(hot);
        waterSupply.setDebt(debt);
        waterSupply.setActive(active);

        return objectMapper.convertValue(waterSupplyRepository.save(waterSupply), WaterSupplyDTO.class);
    }

    @Override
    public WaterSupplyDTO findByApartmentId(Integer apartmentId) {
        var waterSupplyPE = waterSupplyRepository.findById(apartmentId);
        return waterSupplyPE.map(waterSupply -> objectMapper.convertValue(waterSupply, WaterSupplyDTO.class)).orElse(null);
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
