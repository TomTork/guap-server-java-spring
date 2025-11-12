package ru.anotherworld.server.handler.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityEvent {
    private int apartment_id;
    private long day_consumption;
    private long night_consumption;
    private float debt;
    private boolean active;
}
