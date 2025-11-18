package ru.anotherworld.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.anotherworld.server.handler.event.ApartmentEvent;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.service.ApartmentServiceImpl;

@Service
public class EventHandler {
    @Autowired
    private ApartmentServiceImpl apartmentService;
    private ApartmentEvent apartment;

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
                            System.out.println("Delete apartment: " + apartment.getApartment_id());
                            apartmentService.delete(apartment.getApartment_id());
                            break;
                    }
                    apartmentService.isSend = true;
                    break;
            }
        }
        System.out.println("Event received: " + event);
    }
}