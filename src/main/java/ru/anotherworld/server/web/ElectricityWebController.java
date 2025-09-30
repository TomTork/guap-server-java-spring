package ru.anotherworld.server.web;

import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.ElectricityService;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/electricity")
@RequiredArgsConstructor
public class ElectricityWebController {

    private final ElectricityService electricityService;
    private final ApartmentService apartmentService;

    @GetMapping
    public void listElectricity(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<ElectricityDTO> electricityList = electricityService.listAll();
        List<ApartmentDTO> apartments = apartmentService.listAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Электричество</title>");
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

        out.println("<h1>Электричество</h1>");
        out.println("<a href='/'>Главная</a>");

        out.println("<div class='add-form'>");
        out.println("<h3>Добавить электричество</h3>");
        out.println("<form method='post' action='/electricity/add'>");
        out.println("День: <input type='number' name='day' required><br>");
        out.println("Ночь: <input type='number' name='night' required><br>");
        out.println("Долг: <input type='number' name='debt' value='0'><br>");
        out.println("Активно: <input type='checkbox' name='active' checked><br>");
        out.println("Квартира: <select name='apartmentId' required>");
        out.println("<option value=''>Выберите квартиру</option>");
        for (ApartmentDTO apartment : apartments) {
            out.println("<option value='" + apartment.getId() + "'>кв." + apartment.getNumber() + "</option>");
        }
        out.println("</select><br>");
        out.println("<button type='submit'>Создать</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<h3>Список электричества</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>День</th><th>Ночь</th><th>Долг</th><th>Активно</th><th>Действия</th></tr>");

        for (ElectricityDTO electricity : electricityList) {
            out.println("<tr>");
            out.println("<td>" + electricity.getId() + "</td>");
            out.println("<td>" + electricity.getDay() + "</td>");
            out.println("<td>" + electricity.getNight() + "</td>");
            out.println("<td>" + electricity.getDebt() + "</td>");
            out.println("<td>" + (electricity.getActive() ? "да" : "нет") + "</td>");
            out.println("<td>");
            out.println("<form method='post' action='/electricity/delete/" + electricity.getId() + "' style='display: inline;'>");
            out.println("<button type='submit' onclick='return confirm(\"удалить?\")'>Удалить</button>");
            out.println("</form>");
            out.println("<button onclick='showUpdateForm(" + electricity.getId() + ", " + electricity.getDay() + ", " + electricity.getNight() + ", " + electricity.getDebt() + ", " + (electricity.getActive() ? "true" : "false") + ")'>изменить</button>");
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<div id='updateForm' style='display: none; position: fixed; top:50%; left:50%; transform:translate(-50%, -50%); box-shadow:0 0 20px rgba(0,0,0,0.5); z-index: 50; background: #f0f0f0; padding: 15px; margin: 10px 0;'>");
        out.println("<h3>Изменить показания электричества</h3>");
        out.println("<form method='post' action='/electricity/update'>");
        out.println("<input type='hidden' name='id' id='updateId'>");
        out.println("День: <input type='number' step='0.01' name='day' id='updateDay' required><br>");
        out.println("Ночь: <input type='number' step='0.01' name='night' id='updateNight' required><br>");
        out.println("Долг: <input type='number' step='0.01' name='debt' id='updateDebt'><br>");
        out.println("Активно: <input type='checkbox' name='active' id='updateActive'><br>");
        out.println("<button type='submit'>сохранить</button>");
        out.println("<button type='button' onclick='hideUpdateForm()'>отмена</button>");
        out.println("</form>");
        out.println("</div>");
        
        out.println("<script>");
        out.println("function showUpdateForm(id, day, night, debt, active) {");
        out.println("  document.getElementById('updateId').value = id;");
        out.println("  document.getElementById('updateDay').value = day;");
        out.println("  document.getElementById('updateNight').value = night;");
        out.println("  document.getElementById('updateDebt').value = debt;");
        out.println("  document.getElementById('updateActive').checked = active;");
        out.println("  document.getElementById('updateForm').style.display = 'block';");
        out.println("}");
        out.println("function hideUpdateForm() {");
        out.println("  document.getElementById('updateForm').style.display = 'none';");
        out.println("}");
        out.println("</script>");
        
        out.println("</body>");
        out.println("</html>");
    }

    @PostMapping("/add")
    public void addElectricity(@RequestParam Long day,
                              @RequestParam Long night,
                              @RequestParam(defaultValue = "0") Float debt,
                              @RequestParam(defaultValue = "false") Boolean active,
                              @RequestParam Integer apartmentId,
                              HttpServletResponse response) throws IOException {
        try {
            electricityService.add(day, night, debt, active, apartmentId);
            response.sendRedirect("/electricity");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось добавить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/electricity'>назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/update")
    public void updateElectricity(@RequestParam Long day,
                                 @RequestParam Long night,
                                 @RequestParam(defaultValue = "0") Float debt,
                                 @RequestParam(defaultValue = "false") Boolean active,
                                  @RequestParam Integer apartmentId,
                                 HttpServletResponse response) throws IOException {
        try {
            electricityService.update(apartmentId, day, night, debt, active);
            response.sendRedirect("/electricity");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось обновить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/electricity'>назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteElectricity(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        try {
            electricityService.delete(id);
            response.sendRedirect("/electricity");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось удалить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/electricity'>Назад</a>");
            out.println("</body></html>");
        }
    }
}
