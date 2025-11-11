package ru.anotherworld.server.rest;

import ru.anotherworld.server.rest.model.BuildingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.anotherworld.server.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public/rest/buildings")
@RequiredArgsConstructor
public class BuildingRestController {

    private final BuildingService buildingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Operation(summary = "Получить перечень зданий",
            description = "Получить перечень всех зданий в системе. Опциональный параметр flats=1 включает список квартир",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    public ResponseEntity<List<BuildingDTO>> browse(
            @RequestParam(value = "flats", required = false, defaultValue = "0")
            @Parameter(description = "Включить квартиры в ответ (1 - включить, 0 - не включать)") Integer flats) {
        if (flats != null && flats == 1) {
            return ResponseEntity.ok(buildingService.listAllWithApartments());
        } else {
            return ResponseEntity.ok(buildingService.listAll());
        }
    }

    @Operation(summary = "Удаление здания",
            description = "Здание может быть удалено, если с ним не связаны квартиры",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id")
            @Parameter(description = "Идентификатор здания") Integer id) {
        buildingService.delete(id);
    }

    @Operation(summary = "Создать здание",
            description = "Создать новое здание в системе",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    @RequestMapping(value = "/{name}/{code}", method = RequestMethod.POST)
    public ResponseEntity<BuildingDTO> add(
            @PathVariable("name")
            @Parameter(description = "Название здания") String name,
            @PathVariable("code")
            @Parameter(description = "Код здания") String code) {
        return ResponseEntity.ok(buildingService.add(name, code));
    }

    @Operation(summary = "Поиск здания по коду",
            description = "Поиск здания по уникальному коду",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    @RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
    public ResponseEntity<BuildingDTO> findByCode(@PathVariable("code")
            @Parameter(description = "Код здания") String code) {
        return ResponseEntity.ok(buildingService.findByCode(code));
    }

    @Operation(summary = "Поиск здания по ID",
            description = "Поиск здания по идентификатору",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BuildingDTO> findById(@PathVariable("id")
            @Parameter(description = "Идентификатор здания") Integer id) {
        return ResponseEntity.ok(buildingService.findById(id));
    }

    @Operation(summary = "Обновить данные здания",
            description = "Обновить данные существующего здания по идентификатору",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401",
                        description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403",
                        description = "Аутентификация предоставлена, но у пользователя нет доступа"),
                @ApiResponse(responseCode = "404",
                        description = "Ресурс не найден")
            })
    @RequestMapping(value = "/{id}/{name}/{code}", method = RequestMethod.PUT)
    public ResponseEntity<BuildingDTO> update(
            @PathVariable("id")
            @Parameter(description = "Идентификатор здания") Integer id,
            @PathVariable("name")
            @Parameter(description = "Название здания") String name,
            @PathVariable("code")
            @Parameter(description = "Код здания") String code) {
        return ResponseEntity.ok(buildingService.update(id, name, code));
    }
}
