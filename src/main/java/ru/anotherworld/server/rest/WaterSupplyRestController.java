package ru.anotherworld.server.rest;

import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.anotherworld.server.service.WaterSupplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public/rest/watersupply")
@RequiredArgsConstructor
public class WaterSupplyRestController {

    private final WaterSupplyService waterSupplyService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Operation(summary = "Получить перечень записей водоснабжения",
            description = "Получить перечень всех записей потребления воды в системе",
            responses = {
                @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403", description = "Нет доступа"),
                @ApiResponse(responseCode = "404", description = "Ресурс не найден")
            })
    public ResponseEntity<List<WaterSupplyDTO>> browse() {
        return ResponseEntity.ok(waterSupplyService.listAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление записи водоснабжения", description = "Удалить запись потребления воды по ID")
    public void delete(@PathVariable("id") @Parameter(description = "ID записи") Integer id) {
        waterSupplyService.delete(id);
    }

    @RequestMapping(value = "/{cold}/{hot}/{debt}/{active}/{apartmentId}", method = RequestMethod.PUT)
    @Operation(summary = "Создать новую запись водоснабжения", description = "Создать запись потребления воды для квартиры")
    public ResponseEntity<WaterSupplyDTO> add(
            @PathVariable("cold") @Parameter(description = "Холодная вода") Long cold,
            @PathVariable("hot") @Parameter(description = "Горячая вода") Long hot,
            @PathVariable("debt") @Parameter(description = "Задолженность") Float debt,
            @PathVariable("active") @Parameter(description = "Активность (true/false)") Boolean active,
            @PathVariable("apartmentId") @Parameter(description = "ID квартиры") Integer apartmentId) {
        return ResponseEntity.ok(waterSupplyService.add(cold, hot, debt, active, apartmentId));
    }

    @RequestMapping(value = "/{cold}/{hot}/{debt}/{active}/{apartmentId}", method = RequestMethod.POST)
    @Operation(summary = "Обновить существующую запись водоснабжения", description = "Обновить запись потребления воды для квартиры")
    public ResponseEntity<WaterSupplyDTO> update(
            @PathVariable("cold") @Parameter(description = "Холодная вода") Long cold,
            @PathVariable("hot") @Parameter(description = "Горячая вода") Long hot,
            @PathVariable("debt") @Parameter(description = "Задолженность") Float debt,
            @PathVariable("active") @Parameter(description = "Активность (true/false)") Boolean active,
            @PathVariable("apartmentId") @Parameter(description = "ID квартиры") Integer apartmentId) {
        return ResponseEntity.ok(waterSupplyService.update(apartmentId, cold, hot, debt, active));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "Поиск записи водоснабжения по ID", description = "Найти запись потребления воды по идентификатору")
    public ResponseEntity<WaterSupplyDTO> findById(@PathVariable("id") @Parameter(description = "ID записи") Integer id) {
        return ResponseEntity.ok(waterSupplyService.findById(id));
    }

    @RequestMapping(value = "/apartment/{apartmentId}", method = RequestMethod.GET)
    @Operation(summary = "Запись водоснабжения для квартиры", description = "Получить запись потребления воды для указанной квартиры")
    public ResponseEntity<WaterSupplyDTO> findByApartmentId(@PathVariable("apartmentId") @Parameter(description = "ID квартиры") Integer apartmentId) {
        return ResponseEntity.ok(waterSupplyService.findByApartmentId(apartmentId));
    }

    @RequestMapping(value = "/active/{active}", method = RequestMethod.GET)
    @Operation(summary = "Записи водоснабжения по статусу", description = "Получить все записи потребления воды по статусу активности")
    public ResponseEntity<List<WaterSupplyDTO>> findByActive(@PathVariable("active") @Parameter(description = "Статус активности") Boolean active) {
        return ResponseEntity.ok(waterSupplyService.findByActive(active));
    }
}
