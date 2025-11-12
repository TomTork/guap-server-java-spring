package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.ApartmentRepository;
import ru.anotherworld.server.db.dao.BuildingRepository;
import ru.anotherworld.server.db.model.ApartmentPE;
import ru.anotherworld.server.db.model.BuildingPE;
import ru.anotherworld.server.handler.EventSender;
import ru.anotherworld.server.handler.event.ApartmentEvent;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    public boolean isSend = true;

    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;
    private final ObjectMapper objectMapper;
    private final EventSender eventSender;

    @Override
    public List<ApartmentDTO> listAll() {
        return apartmentRepository.findAll().stream()
                .map(this::convertToApartmentDTO)
                .collect(Collectors.toList());
    }

    private ApartmentDTO convertToApartmentDTO(ApartmentPE apartmentPE) {
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(apartmentPE.getId());
        apartmentDTO.setNumber(apartmentPE.getNumber());
        apartmentDTO.setTotalSquare(apartmentPE.getTotalSquare());
        apartmentDTO.setLivingSquare(apartmentPE.getLivingSquare());
        apartmentDTO.setRoomsAmount(apartmentPE.getRoomsAmount());
        apartmentDTO.setFloor(apartmentPE.getFloor());
        
        if (apartmentPE.getBuilding() != null) {
            apartmentDTO.setBuildingId(apartmentPE.getBuilding().getId());
            apartmentDTO.setBuildingName(apartmentPE.getBuilding().getName());
        }
        
        if (apartmentPE.getElectricity() != null) {
            ElectricityDTO electricityDTO = new ElectricityDTO();
            electricityDTO.setId(apartmentPE.getElectricity().getId());
            electricityDTO.setDay(apartmentPE.getElectricity().getDay());
            electricityDTO.setNight(apartmentPE.getElectricity().getNight());
            electricityDTO.setDebt(apartmentPE.getElectricity().getDebt());
            electricityDTO.setActive(apartmentPE.getElectricity().getActive());
            apartmentDTO.setElectricity(electricityDTO);
        }
        
        if (apartmentPE.getWaterSupply() != null) {
            WaterSupplyDTO waterSupplyDTO = new WaterSupplyDTO();
            waterSupplyDTO.setId(apartmentPE.getWaterSupply().getId());
            waterSupplyDTO.setCold(apartmentPE.getWaterSupply().getCold());
            waterSupplyDTO.setHot(apartmentPE.getWaterSupply().getHot());
            waterSupplyDTO.setDebt(apartmentPE.getWaterSupply().getDebt());
            waterSupplyDTO.setActive(apartmentPE.getWaterSupply().getActive());
            apartmentDTO.setWaterSupply(waterSupplyDTO);
        }
        
        return apartmentDTO;
    }

    @Override
    public void delete(Integer id) {
        ApartmentEvent event = new ApartmentEvent(id, "", 0, 0, 0, 0, 0);

        try {
            if (isSend) {
                eventSender.create(event, "delete");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send delete event", e);
        }
        apartmentRepository.deleteById(id);
    }

    @Override
    public ApartmentDTO add(String number, Float totalSquare, Float livingSquare, Integer roomsAmount, Integer floor, Integer buildingId) {
        BuildingPE building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        ApartmentPE apartment = new ApartmentPE(number, totalSquare, livingSquare, roomsAmount, floor, building);
        ApartmentDTO result = convertToApartmentDTO(apartmentRepository.save(apartment));
        try {
            if (isSend) {
                eventSender.create(result, "create");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ApartmentDTO update(Integer id, String number, Float totalSquare, Float livingSquare, Integer roomsAmount, Integer floor, Integer buildingId) {
        ApartmentPE apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found with id: " + id));
                
        BuildingPE building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));
                
        apartment.setNumber(number);
        apartment.setTotalSquare(totalSquare);
        apartment.setLivingSquare(livingSquare);
        apartment.setRoomsAmount(roomsAmount);
        apartment.setFloor(floor);
        apartment.setBuilding(building);

        ApartmentDTO result = convertToApartmentDTO(apartmentRepository.save(apartment));
        try {
            if (isSend) {
                eventSender.create(result, "update");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ApartmentDTO findByNumber(String number) {
        var apartmentPE = apartmentRepository.findByNumber(number);
        return apartmentPE.map(this::convertToApartmentDTO).orElse(null);
    }

    @Override
    public ApartmentDTO findById(Integer id) {
        var apartmentPE = apartmentRepository.findById(id);
        return apartmentPE.map(this::convertToApartmentDTO).orElse(null);
    }

    @Override
    public List<ApartmentDTO> findByBuildingId(Integer buildingId) {
        BuildingPE building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        return apartmentRepository.findByBuilding(building).stream()
                .map(this::convertToApartmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentDTO> findByFloor(Integer floor) {
        return apartmentRepository.findByFloor(floor).stream()
                .map(this::convertToApartmentDTO)
                .collect(Collectors.toList());
    }
}
