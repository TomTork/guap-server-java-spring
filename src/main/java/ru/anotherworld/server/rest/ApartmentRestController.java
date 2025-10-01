/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package ru.anotherworld.server.rest;

import ru.anotherworld.server.rest.model.ApartmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.anotherworld.server.service.ApartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public/rest/apartments")
@RequiredArgsConstructor
public class ApartmentRestController {

    private final ApartmentService apartmentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Operation(summary = "Получить перечень квартир",
            description = "Получить перечень всех квартир в системе",
            responses = {
                @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
                @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                @ApiResponse(responseCode = "403", description = "Нет доступа"),
                @ApiResponse(responseCode = "404", description = "Ресурс не найден")
            })
    public ResponseEntity<List<ApartmentDTO>> browse() {
        return ResponseEntity.ok(apartmentService.listAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление квартиры", description = "Удалить квартиру по ID")
    public void delete(@PathVariable("id") @Parameter(description = "ID квартиры") Integer id) {
        apartmentService.delete(id);
    }

    @RequestMapping(value = "/{number}/{totalSquare}/{livingSquare}/{roomsAmount}/{floor}/{buildingId}", method = RequestMethod.POST)
    @Operation(summary = "Создать или обновить квартиру", description = "Создать или обновить квартиру в указанном здании")
    public ResponseEntity<ApartmentDTO> add(
            @PathVariable("number") @Parameter(description = "Номер квартиры") String number,
            @PathVariable("totalSquare") @Parameter(description = "Общая площадь") Float totalSquare,
            @PathVariable("livingSquare") @Parameter(description = "Жилая площадь") Float livingSquare,
            @PathVariable("roomsAmount") @Parameter(description = "Количество комнат") Integer roomsAmount,
            @PathVariable("floor") @Parameter(description = "Этаж") Integer floor,
            @PathVariable("buildingId") @Parameter(description = "ID здания") Integer buildingId) {
        return ResponseEntity.ok(apartmentService.add(number, totalSquare, livingSquare, roomsAmount, floor, buildingId));
    }

    @RequestMapping(value = "/number/{number}", method = RequestMethod.GET)
    @Operation(summary = "Поиск квартиры по номеру", description = "Найти квартиру по номеру")
    public ResponseEntity<ApartmentDTO> findByNumber(@PathVariable("number") @Parameter(description = "Номер квартиры") String number) {
        return ResponseEntity.ok(apartmentService.findByNumber(number));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "Поиск квартиры по ID", description = "Найти квартиру по идентификатору")
    public ResponseEntity<ApartmentDTO> findById(@PathVariable("id") @Parameter(description = "ID квартиры") Integer id) {
        return ResponseEntity.ok(apartmentService.findById(id));
    }

    @RequestMapping(value = "/{id}/{number}/{totalSquare}/{livingSquare}/{roomsAmount}/{floor}/{buildingId}", method = RequestMethod.PUT)
    @Operation(summary = "Обновить данные квартиры", description = "Обновить данные существующей квартиры по ID")
    public ResponseEntity<ApartmentDTO> update(
            @PathVariable("id") @Parameter(description = "ID квартиры") Integer id,
            @PathVariable("number") @Parameter(description = "Номер квартиры") String number,
            @PathVariable("totalSquare") @Parameter(description = "Общая площадь") Float totalSquare,
            @PathVariable("livingSquare") @Parameter(description = "Жилая площадь") Float livingSquare,
            @PathVariable("roomsAmount") @Parameter(description = "Количество комнат") Integer roomsAmount,
            @PathVariable("floor") @Parameter(description = "Этаж") Integer floor,
            @PathVariable("buildingId") @Parameter(description = "ID здания") Integer buildingId) {
        return ResponseEntity.ok(apartmentService.update(id, number, totalSquare, livingSquare, roomsAmount, floor, buildingId));
    }

    @RequestMapping(value = "/building/{buildingId}", method = RequestMethod.GET)
    @Operation(summary = "Квартиры в здании", description = "Получить все квартиры в указанном здании")
    public ResponseEntity<List<ApartmentDTO>> findByBuildingId(@PathVariable("buildingId") @Parameter(description = "ID здания") Integer buildingId) {
        return ResponseEntity.ok(apartmentService.findByBuildingId(buildingId));
    }

    @RequestMapping(value = "/floor/{floor}", method = RequestMethod.GET)
    @Operation(summary = "Квартиры на этаже", description = "Получить все квартиры на указанном этаже")
    public ResponseEntity<List<ApartmentDTO>> findByFloor(@PathVariable("floor") @Parameter(description = "Номер этажа") Integer floor) {
        return ResponseEntity.ok(apartmentService.findByFloor(floor));
    }
}
