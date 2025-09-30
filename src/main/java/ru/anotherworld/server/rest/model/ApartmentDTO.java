package ru.anotherworld.server.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Описывает данные квартиры, передаваемые клиенту
 */
@Data
@Schema(description = "Квартира")
public class ApartmentDTO {

    @Schema(description = "Идентификатор квартиры")
    private Integer id;

    @Schema(description = "Номер квартиры")
    private String number;

    @Schema(description = "Общая площадь")
    private Float totalSquare;

    @Schema(description = "Жилая площадь")
    private Float livingSquare;

    @Schema(description = "Количество комнат")
    private Integer roomsAmount;

    @Schema(description = "Этаж")
    private Integer floor;

    @Schema(description = "Здание")
    private BuildingDTO building;

    @Schema(description = "Данные по электроэнергии")
    private ElectricityDTO electricity;

    @Schema(description = "Данные по водоснабжению")
    private WaterSupplyDTO waterSupply;
}
