package ru.anotherworld.server.web;

import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.service.BuildingService;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import ru.anotherworld.server.rest.model.BuildingDTO;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentWebController {

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;

    @GetMapping
    public void listApartments(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<ApartmentDTO> apartments = apartmentService.listAll();
        List<BuildingDTO> buildings = buildingService.listAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>квартиры</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid black; padding: 8px; }");
        out.println("th { background: #ddd; }");
        out.println("input, select { padding: 5px; margin: 5px; }");
        out.println("button { padding: 5px 10px; margin: 2px; }");
        out.println(".add-form { background: #f5f5f5; padding: 10px; margin: 10px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Квартиры</h1>");
        out.println("<a href='/'>Главная</a>");

        out.println("<div class='add-form'>");
        out.println("<h3>Добавить квартиру</h3>");
        out.println("<form method='post' action='/apartments/add'>");
        out.println("Номер квартиры: <input type='text' name='number' required><br>");
        out.println("Общая площадь: <input type='number' name='totalSquare' required><br>");
        out.println("Жилая площадь: <input type='number' name='livingSquare' required><br>");
        out.println("Количество комнат: <input type='number' name='roomsAmount' required><br>");
        out.println("Этаж: <input type='number' name='floor' required><br>");
        out.println("Здание: <select name='buildingId' required>");
        out.println("<option value=''>Выберите здание</option>");
        for (BuildingDTO building : buildings) {
            out.println("<option value='" + building.getId() + "'>" + building.getName() + "</option>");
        }
        out.println("</select><br>");
        out.println("<button type='submit'>Создать</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<h3>список квартир</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>№</th><th>Общ.пл.</th><th>Жил.пл.</th><th>Комнат</th><th>Этаж</th><th>Здание</th><th>Электричество</th><th>Водоснабжение</th><th>Действие</th></tr>");

        for (ApartmentDTO apartment : apartments) {
            out.println("<tr>");
            out.println("<td>" + apartment.getId() + "</td>");
            out.println("<td>" + apartment.getNumber() + "</td>");
            out.println("<td>" + apartment.getTotalSquare() + "</td>");
            out.println("<td>" + apartment.getLivingSquare() + "</td>");
            out.println("<td>" + apartment.getRoomsAmount() + "</td>");
            out.println("<td>" + apartment.getFloor() + "</td>");
            out.println("<td>" + (apartment.getBuildingName() != null ? apartment.getBuildingName() : "—") + "</td>");
            
            out.print("<td>");
            if (apartment.getElectricity() != null) {
                out.print("День: " + apartment.getElectricity().getDay() + 
                         ", Ночь: " + apartment.getElectricity().getNight() +
                         ", Долг: " + apartment.getElectricity().getDebt());
            } else {
                out.print("—");
            }
            out.println("</td>");
            
            out.print("<td>");
            if (apartment.getWaterSupply() != null) {
                out.print("Хол.: " + apartment.getWaterSupply().getCold() + 
                         ", Гор.: " + apartment.getWaterSupply().getHot() +
                         ", Долг: " + apartment.getWaterSupply().getDebt());
            } else {
                out.print("—");
            }
            out.println("</td>");
            
            out.println("<td>");
            out.println("<form method='post' action='/apartments/delete/" + apartment.getId() + "' style='display: inline;'>");
            out.println("<button type='submit' onclick='return confirm(\"Вы уверены, что хотите удалить?\")'>Удалить</button>");
            out.println("</form>");
            out.println("<button onclick='showUpdateForm(" + apartment.getId() + ", \"" + apartment.getNumber() + "\", " + apartment.getTotalSquare() + ", " + apartment.getLivingSquare() + ", " + apartment.getRoomsAmount() + ", " + apartment.getFloor() + ", " + (apartment.getBuildingId() != null ? apartment.getBuildingId() : "null") + ")'>изменить</button>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("<div id='updateForm' style='display: none; position: fixed; top:50%; left:50%; transform:translate(-50%, -50%); box-shadow:0 0 20px rgba(0,0,0,0.5); z-index: 50; background: #f0f0f0; padding: 15px; margin: 10px 0;'>");
        out.println("<h3>Изменить квартиру</h3>");
        out.println("<form method='post' action='/apartments/update' id='updateApartmentForm'>");
        out.println("<input type='hidden' name='id' id='updateId'>");
        out.println("Номер квартиры: <input type='text' name='number' id='updateNumber' required><br>");
        out.println("Общая площадь: <input type='number' name='totalSquare' id='updateTotalSquare' required><br>");
        out.println("Жилая площадь: <input type='number' name='livingSquare' id='updateLivingSquare' required><br>");
        out.println("Количество комнат: <input type='number' name='roomsAmount' id='updateRoomsAmount' required><br>");
        out.println("Этаж: <input type='number' name='floor' id='updateFloor' required><br>");
        out.println("Здание: <select name='buildingId' id='updateBuildingId' required>");
        out.println("<option value=''>Выберите здание</option>");
        for (BuildingDTO building : buildings) {
            out.println("<option value='" + building.getId() + "'>" + building.getName() + "</option>");
        }
        out.println("</select><br>");
        out.println("<button type='submit'>Сохранить</button>");
        out.println("<button type='button' onclick='hideUpdateForm()'>Отмена</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<script>");
        out.println("function showUpdateForm(id, number, totalSquare, livingSquare, roomsAmount, floor, buildingId) {");
        out.println("  document.getElementById('updateId').value = id;");
        out.println("  document.getElementById('updateNumber').value = number;");
        out.println("  document.getElementById('updateTotalSquare').value = totalSquare;");
        out.println("  document.getElementById('updateLivingSquare').value = livingSquare;");
        out.println("  document.getElementById('updateRoomsAmount').value = roomsAmount;");
        out.println("  document.getElementById('updateFloor').value = floor;");
        out.println("  if (buildingId && buildingId !== 'null') document.getElementById('updateBuildingId').value = buildingId;");
        out.println("  document.getElementById('updateForm').style.display = 'block';");
        out.println("}");
        out.println("function hideUpdateForm() {");
        out.println("  document.getElementById('updateForm').style.display = 'none';");
        out.println("}");
        out.println("</script>");

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    @PostMapping("/add")
    public void addApartment(@RequestParam String number,
                           @RequestParam Float totalSquare,
                           @RequestParam Float livingSquare,
                           @RequestParam Integer roomsAmount,
                           @RequestParam Integer floor,
                           @RequestParam Integer buildingId,
                           HttpServletResponse response) throws IOException {
        try {
            apartmentService.add(number, totalSquare, livingSquare, roomsAmount, floor, buildingId);
            response.sendRedirect("/apartments");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось добавить квартиру</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/apartments'>Назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/update")
    public void updateApartment(@RequestParam Integer id,
                                @RequestParam String number,
                                @RequestParam Float totalSquare,
                                @RequestParam Float livingSquare,
                                @RequestParam Integer roomsAmount,
                                @RequestParam Integer floor,
                                @RequestParam Integer buildingId,
                                HttpServletResponse response) throws IOException {
        try {
            apartmentService.update(id, number, totalSquare, livingSquare, roomsAmount, floor, buildingId);
            response.sendRedirect("/apartments");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось обновить квартиру</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/apartments'>Назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteApartment(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        try {
            apartmentService.delete(id);
            response.sendRedirect("/apartments");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось удалить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/apartments'>Назад</a>");
            out.println("</body></html>");
        }
    }
}
