package ru.anotherworld.server.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * Описывает данные здания, передаваемые клиенту
 */
@Data
@Schema(description = "Здание")
public class BuildingDTO {

    @Schema(description = "Идентификатор здания")
    private Integer id;

    @Schema(description = "Название здания")
    private String name;

    @Schema(description = "Код здания")
    private String code;

    @Schema(description = "Список квартир в здании")
    private List<ApartmentDTO> apartments;
}
