package ru.anotherworld.server.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.anotherworld.server.handler.event.ApartmentEvent;
import ru.anotherworld.server.handler.event.BuildingEvent;
import ru.anotherworld.server.handler.event.ElectricityEvent;
import ru.anotherworld.server.handler.event.WaterSupplyEvent;
import ru.anotherworld.server.service.*;

@Service
public class EventHandler {
    @Autowired
    private ApartmentServiceImpl apartmentService;
    @Autowired
    private BuildingServiceImpl buildingService;
    @Autowired
    private ElectricityServiceImpl electricityService;
    @Autowired
    private WaterSupplyServiceImpl waterSupplyService;
    private ApartmentEvent apartment;
    private BuildingEvent building;
    private WaterSupplyEvent waterSupply;
    private ElectricityEvent electricity;

    @Value("${type.server2}")
    private String server2;

    @KafkaListener(topics = "lab-work")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(ConsumerRecord<String, Object> event) {
        String[] keys = event.key().split(",");
        if (keys[2].equals(server2)) {
            switch (keys[0]) {
                case "apartment":
                    apartmentService.isSend = false;
                    try {
                        apartment = (ApartmentEvent) event.value();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    switch (keys[1]) {
                        case "create":
                            apartmentService.add(
                                    apartment.getApartment_number(),
                                    (float) apartment.getTotal_square(),
                                    (float) apartment.getLiving_square(),
                                    apartment.getRooms_amount(),
                                    apartment.getFloor(),
                                    apartment.getBuilding_id()
                            );
                            break;
                        case "update":
                            apartmentService.update(
                                    apartment.getApartment_id(),
                                    apartment.getApartment_number(),
                                    (float) apartment.getTotal_square(),
                                    (float) apartment.getLiving_square(),
                                    apartment.getRooms_amount(),
                                    apartment.getFloor(),
                                    apartment.getBuilding_id()
                            );
                            break;
                        case "delete":
                            apartmentService.delete(apartment.getApartment_id());
                            break;
                    }
                    apartmentService.isSend = true;
                    break;
                case "building":
                    buildingService.isSend = false;
                    try {
                        building = (BuildingEvent) event.value();
                    } catch (Exception e) {
                        throw  new RuntimeException(e);
                    }
                    switch (keys[1]) {
                        case "create":
                            buildingService.add(
                                    building.getBuilding_name(),
                                    building.getBuilding_code()
                            );
                            break;
                        case "update":
                            buildingService.update(
                                    building.getBuilding_id(),
                                    building.getBuilding_name(),
                                    building.getBuilding_code()
                            );
                            break;
                        case "delete":
                            buildingService.delete(building.getBuilding_id());
                            break;
                    }
                    buildingService.isSend = true;
                    break;
                case "water-supply":
                    waterSupplyService.isSend = false;
                    try {
                        waterSupply = (WaterSupplyEvent) event.value();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    switch (keys[1]) {
                        case "create":
                            waterSupplyService.add(
                                    waterSupply.getCold_consumption(),
                                    waterSupply.getHot_consumption(),
                                    waterSupply.getDebt(),
                                    true,
                                    waterSupply.getApartment_id()
                            );
                            break;
                        case "update":
                            waterSupplyService.update(
                                    waterSupply.getApartment_id(),
                                    waterSupply.getCold_consumption(),
                                    waterSupply.getHot_consumption(),
                                    waterSupply.getDebt(),
                                    true
                            );
                            break;
                        case "delete":
                            waterSupplyService.delete(waterSupply.getApartment_id());
                            break;
                    }
                    waterSupplyService.isSend = true;
                    break;
                case "electricity":
                    electricityService.isSend = false;
                    try {
                        electricity = (ElectricityEvent) event.value();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    switch (keys[1]) {
                        case "create":
                            electricityService.add(
                                    electricity.getDay_consumption(),
                                    electricity.getNight_consumption(),
                                    electricity.getDebt(),
                                    true,
                                    electricity.getApartment_id()
                            );
                            break;
                        case "update":
                            electricityService.update(
                                    electricity.getApartment_id(),
                                    electricity.getDay_consumption(),
                                    electricity.getNight_consumption(),
                                    electricity.getDebt(),
                                    true
                            );
                            break;
                        case "delete":
                            electricityService.delete(electricity.getApartment_id());
                            break;
                    }
                    electricityService.isSend = true;
                    break;
            }
        }
        System.out.println("Event received: " + event);
    }
}