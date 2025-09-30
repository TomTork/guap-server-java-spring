package ru.anotherworld.server.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Описывает, что передаётся клиенту и каким образом передаётся
 */
@Data
@Schema(description = "Факультет")
public class SchoolDTO {

    @Schema(description = "Идентификатор факультета")
    private Integer id;
    @Schema(description = "Номер факультета")
    private Integer number;
    @Schema(description = "Название факультета")
    private String name;
}
