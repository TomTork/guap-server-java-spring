package ru.anotherworld.server.handler.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingEvent {
    private int building_id;
    private String building_name;
    private String building_code;
}
