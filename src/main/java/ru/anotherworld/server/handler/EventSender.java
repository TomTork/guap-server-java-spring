package ru.anotherworld.server.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.anotherworld.server.handler.event.ApartmentEvent;
import ru.anotherworld.server.handler.event.BuildingEvent;
import ru.anotherworld.server.handler.event.ElectricityEvent;
import ru.anotherworld.server.handler.event.WaterSupplyEvent;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import ru.anotherworld.server.rest.model.BuildingDTO;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class EventSender {
    @Value("${type.server}")
    private String serverType;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public String create(ApartmentDTO apartmentDTO, String typeAction) throws ExecutionException, InterruptedException {
        ApartmentEvent apartmentCreatedEvent = new ApartmentEvent(
                apartmentDTO.getId(),
                apartmentDTO.getNumber(),
                apartmentDTO.getTotalSquare(),
                apartmentDTO.getLivingSquare(),
                apartmentDTO.getRoomsAmount(),
                apartmentDTO.getFloor(),
                apartmentDTO.getBuildingId()
        );

        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "apartment," + typeAction + "," + serverType, apartmentCreatedEvent).get();

        return apartmentDTO.getId().toString();
    }
    public String create(ApartmentEvent apartmentDTO, String typeAction) throws ExecutionException, InterruptedException {
        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "apartment," + typeAction + "," + serverType, apartmentDTO).get();
        return "";
    }
    public String create(BuildingDTO buildingDTO, String typeAction) throws ExecutionException, InterruptedException {
        BuildingEvent buildingCreatedEvent = new BuildingEvent(
                buildingDTO.getId(),
                buildingDTO.getName(),
                buildingDTO.getCode()
        );

        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "building," + typeAction + "," + serverType, buildingCreatedEvent).get();

        return buildingDTO.getId().toString();
    }
    public String create(BuildingEvent buildingDTO, String typeAction) throws ExecutionException, InterruptedException {
        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "building," + typeAction + "," + serverType, buildingDTO).get();
        return "";
    }
    public String create(ElectricityDTO electricityDTO, String typeAction) throws ExecutionException, InterruptedException {
        ElectricityEvent electricityCreatedEvent = new ElectricityEvent(
                electricityDTO.getId(),
                electricityDTO.getDay(),
                electricityDTO.getNight(),
                electricityDTO.getDebt(),
                electricityDTO.getActive()
        );

        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "electricity," + typeAction + "," + serverType, electricityCreatedEvent).get();

        return electricityDTO.getId().toString();
    }
    public String create(ElectricityEvent electricityDTO, String typeAction) throws ExecutionException, InterruptedException {
        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "electricity," + typeAction + "," + serverType, electricityDTO).get();
        return "";
    }
    public String create(WaterSupplyDTO waterSupplyDTO, String typeAction) throws ExecutionException, InterruptedException {
        WaterSupplyEvent waterSupplyCreatedEvent = new WaterSupplyEvent(
                waterSupplyDTO.getId(),
                waterSupplyDTO.getCold(),
                waterSupplyDTO.getHot(),
                waterSupplyDTO.getDebt(),
                waterSupplyDTO.getActive()
        );

        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "water-supply," + typeAction + "," + serverType, waterSupplyCreatedEvent).get();

        return waterSupplyDTO.getId().toString();
    }
    public String create(WaterSupplyEvent waterSupplyDTO, String typeAction) throws ExecutionException, InterruptedException {
        SendResult<String, Object> result = kafkaTemplate
                .send("lab-work", "water-supply," + typeAction + "," + serverType, waterSupplyDTO).get();
        return "";
    }
}
