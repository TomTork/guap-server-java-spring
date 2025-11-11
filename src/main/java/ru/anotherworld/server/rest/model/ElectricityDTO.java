package ru.anotherworld.server.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Описывает данные потребления электроэнергии, передаваемые клиенту
 */
@Data
@Schema(description = "Электроэнергия")
public class ElectricityDTO {

    @Schema(description = "Идентификатор записи")
    private Integer id;

    @Schema(description = "Дневное потребление")
    private Long day;

    @Schema(description = "Ночное потребление")
    private Long night;

    @Schema(description = "Задолженность", defaultValue = "0")
    private Float debt;

    @Schema(description = "Активность", defaultValue = "true")
    private Boolean active;
}
