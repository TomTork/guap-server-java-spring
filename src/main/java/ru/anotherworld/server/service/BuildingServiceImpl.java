package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import ru.anotherworld.server.db.dao.BuildingRepository;
import ru.anotherworld.server.db.model.BuildingPE;
import ru.anotherworld.server.rest.model.BuildingDTO;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<BuildingDTO> listAll() {
        return buildingRepository.findAll().stream()
                .map(this::convertToBuildingDTO)
                .collect(Collectors.toList());
    }

    private BuildingDTO convertToBuildingDTO(BuildingPE buildingPE) {
        BuildingDTO buildingDTO = new BuildingDTO();
        buildingDTO.setId(buildingPE.getId());
        buildingDTO.setName(buildingPE.getName());
        buildingDTO.setCode(buildingPE.getCode());
        return buildingDTO;
    }

    @Override
    public List<BuildingDTO> listAllWithApartments() {
        return buildingRepository.findAll().stream()
                .map(this::convertToBuildingDTOWithApartments)
                .collect(Collectors.toList());
    }

    private BuildingDTO convertToBuildingDTOWithApartments(BuildingPE buildingPE) {
        BuildingDTO buildingDTO = new BuildingDTO();
        buildingDTO.setId(buildingPE.getId());
        buildingDTO.setName(buildingPE.getName());
        buildingDTO.setCode(buildingPE.getCode());
        
        List<ApartmentDTO> apartmentDTOs = buildingPE.getApartments().stream()
                .map(apartmentPE -> {
                    ApartmentDTO apartmentDTO = objectMapper.convertValue(apartmentPE, ApartmentDTO.class);
//                    apartmentDTO.setBuilding(null);
                    return apartmentDTO;
                })
                .collect(Collectors.toList());
        
        buildingDTO.setApartments(apartmentDTOs);
        return buildingDTO;
    }

    @Override
    public void delete(Integer id) {
        buildingRepository.deleteById(id);
    }

    @Override
    public BuildingDTO add(String name, String code) {
        return objectMapper.convertValue(
            buildingRepository.save(new BuildingPE(name, code)),
            BuildingDTO.class
        );
    }

    @Override
    public BuildingDTO update(Integer id, String name, String code) {
        BuildingPE building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + id));
        
        building.setName(name);
        building.setCode(code);
        
        return objectMapper.convertValue(buildingRepository.save(building), BuildingDTO.class);
    }

    @Override
    public BuildingDTO findByCode(String code) {
        var buildingPE = buildingRepository.findByCode(code);
        return buildingPE.map(this::convertToBuildingDTO).orElse(null);
    }

    @Override
    public BuildingDTO findById(Integer id) {
        var buildingPE = buildingRepository.findById(id);
        return buildingPE.map(this::convertToBuildingDTO).orElse(null);
    }
}
