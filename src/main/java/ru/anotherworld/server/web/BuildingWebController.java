package ru.anotherworld.server.web;

import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.BuildingService;
import ru.anotherworld.server.rest.model.BuildingDTO;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingWebController {

    private final BuildingService buildingService;

    @GetMapping
    public void listBuildings(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<BuildingDTO> buildings = buildingService.listAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Здания</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid black; padding: 8px; }");
        out.println("th { background: #ddd; }");
        out.println("input { padding: 5px; margin: 5px; }");
        out.println("button { padding: 5px 10px; margin: 2px; }");
        out.println(".add-form { background: #f5f5f5; padding: 10px; margin: 10px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Здания</h1>");
        out.println("<a href='/'>Главная</a>");

        out.println("<div class='add-form'>");
        out.println("<h3>Добавить здание</h3>");
        out.println("<form method='post' action='/buildings/add'>");
        out.println("название: <input type='text' name='name' required>");
        out.println("код: <input type='text' name='code' required>");
        out.println("<button type='submit'>Добавить</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("<h3>Список зданий</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Название</th><th>Код</th><th>Действия</th></tr>");

        for (BuildingDTO building : buildings) {
            out.println("<tr>");
            out.println("<td>" + building.getId() + "</td>");
            out.println("<td>" + building.getName() + "</td>");
            out.println("<td>" + building.getCode() + "</td>");
            out.println("<td>");
            out.println("<form method='post' action='/buildings/delete/" + building.getId() + "' style='display: inline;'>");
            out.println("<button type='submit' onclick='return confirm(\"Точно удалить?\")'>Удалить</button>");
            out.println("</form>");
            out.println("<button onclick='showUpdateForm(" + building.getId() + ", \"" + building.getName() + "\", \"" + building.getCode() + "\")'>изменить</button>");
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<div id='updateForm' style='display: none; position: fixed; top:50%; left:50%; transform:translate(-50%, -50%); box-shadow:0 0 20px rgba(0,0,0,0.5); z-index: 50; background: #f0f0f0; padding: 15px; margin: 10px 0;'>");
        out.println("<h3>Изменить здание</h3>");
        out.println("<form method='post' action='/buildings/update' id='updateBuildingForm'>");
        out.println("<input type='hidden' name='id' id='updateId'>");
        out.println("название: <input type='text' name='name' id='updateName' required>");
        out.println("код: <input type='text' name='code' id='updateCode' required>");
        out.println("<button type='submit'>Сохранить</button>");
        out.println("<button type='button' onclick='hideUpdateForm()'>Отмена</button>");
        out.println("</form>");
        out.println("</div>");
        
        out.println("<script>");
        out.println("function showUpdateForm(id, name, code) {");
        out.println("  document.getElementById('updateId').value = id;");
        out.println("  document.getElementById('updateName').value = name;");
        out.println("  document.getElementById('updateCode').value = code;");
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
    public void addBuilding(@RequestParam String name, @RequestParam String code,
                           HttpServletResponse response) throws IOException {
        try {
            buildingService.add(name, code);
            response.sendRedirect("/buildings");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Что-то пошло не так</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/buildings'>Назад к зданиям</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/update")
    public void updateBuilding(@RequestParam Integer id, @RequestParam String name, @RequestParam String code,
                              HttpServletResponse response) throws IOException {
        try {
            buildingService.update(id, name, code);
            response.sendRedirect("/buildings");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось обновить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/buildings'>Назад к зданиям</a>");
            out.println("</body></html>");
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteBuilding(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        try {
            buildingService.delete(id);
            response.sendRedirect("/buildings");
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Ошибка</title><meta charset='UTF-8'></head>");
            out.println("<body>");
            out.println("<h1>Не получилось удалить</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='/buildings'>Назад к зданиям</a>");
            out.println("</body></html>");
        }
    }
}
