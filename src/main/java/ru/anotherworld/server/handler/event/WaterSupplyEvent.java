package ru.anotherworld.server.handler.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaterSupplyEvent {
    private int apartment_id;
    private long cold_consumption;
    private long hot_consumption;
    private float debt;
    private boolean active;
}
