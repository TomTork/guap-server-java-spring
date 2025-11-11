package ru.anotherworld.server.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Описывает данные потребления водоснабжения, передаваемые клиенту
 */
@Data
@Schema(description = "Водоснабжение")
public class WaterSupplyDTO {

    @Schema(description = "Идентификатор записи")
    private Integer id;

    @Schema(description = "Потребление холодной воды")
    private Long cold;

    @Schema(description = "Потребление горячей воды")
    private Long hot;

    @Schema(description = "Задолженность", defaultValue = "0")
    private Float debt;

    @Schema(description = "Активность", defaultValue = "true")
    private Boolean active;
}
