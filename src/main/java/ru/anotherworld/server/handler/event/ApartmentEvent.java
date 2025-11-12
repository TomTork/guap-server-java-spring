package ru.anotherworld.server.handler.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApartmentEvent {
    private int apartment_id;
    private String apartment_number;
    private double total_square;
    private double living_square;
    private int rooms_amount;
    private int floor;
    private int building_id;
}
