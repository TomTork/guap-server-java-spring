package ru.anotherworld.server.rest;

import ru.anotherworld.server.rest.model.ElectricityDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.anotherworld.server.service.ElectricityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public/rest/electricity")
@RequiredArgsConstructor
public class ElectricityRestController {

    private final ElectricityService electricityService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Operation(summary = "Получить перечень записей электричества",
            description = "Получить перечень всех записей потребления электричества в системе",
            responses = {
                @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403", description = "Нет доступа"),
                @ApiResponse(responseCode = "404", description = "Ресурс не найден")
            })
    public ResponseEntity<List<ElectricityDTO>> browse() {
        return ResponseEntity.ok(electricityService.listAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление записи электричества", description = "Удалить запись потребления электричества по ID")
    public void delete(@PathVariable("id") @Parameter(description = "ID записи") Integer id) {
        electricityService.delete(id);
    }

    @RequestMapping(value = "/{day}/{night}/{debt}/{active}/{apartmentId}", method = RequestMethod.POST)
    @Operation(summary = "Создать новую запись электричества", description = "Создать запись потребления электричества для квартиры")
    public ResponseEntity<ElectricityDTO> add(
            @PathVariable("day") @Parameter(description = "Дневной тариф") Long day,
            @PathVariable("night") @Parameter(description = "Ночной тариф") Long night,
            @PathVariable("debt") @Parameter(description = "Задолженность") Float debt,
            @PathVariable("active") @Parameter(description = "Активность (true/false)") Boolean active,
            @PathVariable("apartmentId") @Parameter(description = "ID квартиры") Integer apartmentId) {
        return ResponseEntity.ok(electricityService.add(day, night, debt, active, apartmentId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "Поиск записи электричества по ID", description = "Найти запись потребления электричества по идентификатору")
    public ResponseEntity<ElectricityDTO> findById(@PathVariable("id") @Parameter(description = "ID записи") Integer id) {
        return ResponseEntity.ok(electricityService.findById(id));
    }

    @RequestMapping(value = "/apartment/{apartmentId}", method = RequestMethod.GET)
    @Operation(summary = "Запись электричества для квартиры", description = "Получить запись потребления электричества для указанной квартиры")
    public ResponseEntity<ElectricityDTO> findByApartmentId(@PathVariable("apartmentId") @Parameter(description = "ID квартиры") Integer apartmentId) {
        return ResponseEntity.ok(electricityService.findByApartmentId(apartmentId));
    }

    @RequestMapping(value = "/active/{active}", method = RequestMethod.GET)
    @Operation(summary = "Записи электричества по статусу", description = "Получить все записи потребления электричества по статусу активности")
    public ResponseEntity<List<ElectricityDTO>> findByActive(@PathVariable("active") @Parameter(description = "Статус активности") Boolean active) {
        return ResponseEntity.ok(electricityService.findByActive(active));
    }
}
