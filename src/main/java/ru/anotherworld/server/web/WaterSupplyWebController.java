package ru.anotherworld.server.web;

import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.WaterSupplyService;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/watersupply")
@RequiredArgsConstructor
public class WaterSupplyWebController {

    private final WaterSupplyService waterSupplyService;
    private final ApartmentService apartmentService;

    @GetMapping
    public void listWaterSupply(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<WaterSupplyDTO> waterSupplyList = waterSupplyService.listAll();
        List<ApartmentDTO> apartments = apartmentService.listAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Водоснабжение</title>");
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

        out.println("<h1>Водоснабжение</h1>");
        out.println("<a href='/'>Главная</a>");

        out.println("<div class='add-form'>");
        out.println("<h3>добавить показания воды</h3>");
        out.println("<form method='post' action='/watersupply/add'>");
        out.println("холодная: <input type='number' step='0.01' name='cold' required><br>");
        out.println("горячая: <input type='number' step='0.01' name='hot' required><br>");
        out.println("долг: <input type='number' step='0.01' name='debt' value='0'><br>");
        out.println("активно: <input type='checkbox' name='active' checked><br>");
        out.println("квартира: <select name='apartmentId' required>");
        out.println("<option value=''>выбери квартиру</option>");
        for (ApartmentDTO apartment : apartments) {
            out.println("<option value='" + apartment.getId() + "'>кв." + apartment.getNumber() + "</option>");
        }
        out.println("</select><br>");
        out.println("<button type='submit'>Обновить</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<h3>показания воды</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Холодная</th><th>Горячая</th><th>Долг</th><th>Активно</th><th>Действия</th></tr>");

        for (WaterSupplyDTO waterSupply : waterSupplyList) {
            out.println("<tr>");
            out.println("<td>" + waterSupply.getId() + "</td>");
            out.println("<td>" + waterSupply.getCold() + "</td>");
            out.println("<td>" + waterSupply.getHot() + "</td>");
            out.println("<td>" + waterSupply.getDebt() + "</td>");
            out.println("<td>" + (waterSupply.getActive() ? "да" : "нет") + "</td>");
            out.println("<td>");
            out.println("<form method='post' action='/watersupply/delete/" + waterSupply.getId() + "' style='display: inline;'>");
            out.println("<button type='submit' onclick='return confirm(\"удалить?\")'>удалить</button>");
            out.println("</form>");
            out.println("<button onclick='showUpdateForm(" + waterSupply.getId() + ", " + waterSupply.getCold() + ", " + waterSupply.getHot() + ", " + waterSupply.getDebt() + ", " + (waterSupply.getActive() ? "true" : "false") + ")'>изменить</button>");
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<div id='updateForm' style='display: none; position: fixed; top:50%; left:50%; transform:translate(-50%, -50%); box-shadow:0 0 20px rgba(0,0,0,0.5); z-index: 50; background: #f0f0f0; padding: 15px; margin: 10px 0;'>");
        out.println("<h3>Изменить показания воды</h3>");
        out.println("<form method='post' action='/watersupply/update'>");
        out.println("<input type='hidden' name='id' id='updateId'>");
        out.println("холодная: <input type='number' step='0.01' name='cold' id='updateCold' required><br>");
        out.println("горячая: <input type='number' step='0.01' name='hot' id='updateHot' required><br>");
        out.println("долг: <input type='number' step='0.01' name='debt' id='updateDebt'><br>");
        out.println("активно: <input type='checkbox' name='active' id='updateActive'><br>");
        out.println("<button type='submit'>сохранить</button>");
        out.println("<button type='button' onclick='hideUpdateForm()'>отмена</button>");
        out.println("</form>");
        out.println("</div>");
        
        out.println("<script>");
        out.println("function showUpdateForm(id, cold, hot, debt, active) {");
        out.println("  document.getElementById('updateId').value = id;");
        out.println("  document.getElementById('updateCold').value = cold;");
        out.println("  document.getElementById('updateHot').value = hot;");
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
    public void addWaterSupply(@RequestParam Long cold,
                              @RequestParam Long hot,
                              @RequestParam(defaultValue = "0") Float debt,
                              @RequestParam(defaultValue = "false") Boolean active,
                              @RequestParam Integer apartmentId,
                              HttpServletResponse response) throws IOException {
        try {
            waterSupplyService.add(cold, hot, debt, active, apartmentId);
            response.sendRedirect("/watersupply");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось добавить воду</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/watersupply'>Назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/update")
    public void updateWaterSupply(@RequestParam Integer id,
                                 @RequestParam Long cold,
                                 @RequestParam Long hot,
                                 @RequestParam(defaultValue = "0") Float debt,
                                 @RequestParam(defaultValue = "false") Boolean active,
                                 HttpServletResponse response) throws IOException {
        try {
            waterSupplyService.update(id, cold, hot, debt, active);
            response.sendRedirect("/watersupply");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось обновить воду</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/watersupply'>Назад</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteWaterSupply(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        try {
            waterSupplyService.delete(id);
            response.sendRedirect("/watersupply");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось удалить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/watersupply'>Назад</a>");
            out.println("</body></html>");
        }
    }
}
