package ru.anotherworld.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/")
public class MainWebController {

    @GetMapping
    public void home(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Лабораторная работа №2. Саиткулов Дмитрий, 4332</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; background: #f0f0f0; }");
        out.println("h1 { color: #333; }");
        out.println("a { color: blue; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println(".menu { background: white; padding: 15px; margin: 10px 0; border: 1px solid #ccc; border-radius: 5px; }");
        out.println(".menu h3 { margin-top: 0; }");
        out.println("ul { list-style: none; padding: 0; }");
        out.println("li { margin: 8px 0; }");
        out.println(".footer { margin-top: 30px; font-size: 12px; color: #666; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Система учета коммунальных услуг</h1>");

        out.println("<div class='menu'>");
        out.println("<h3>Здания</h3>");
        out.println("<a href='/buildings'>редактирование / просмотр</a>");
        out.println("</div>");

        out.println("<div class='menu'>");
        out.println("<h3>Квартиры</h3>");
        out.println("<a href='/apartments'>редактирование / просмотр</a>");
        out.println("</div>");

        out.println("<div class='menu'>");
        out.println("<h3>Электричество</h3>");
        out.println("<a href='/electricity'>редактирование / просмотр</a>");
        out.println("</div>");

        out.println("<div class='menu'>");
        out.println("<h3>Водоснабжение</h3>");
        out.println("<a href='/watersupply'>редактирование / просмотр</a>");
        out.println("</div>");

        out.println("<div class='menu'>");
        out.println("<h3>API</h3>");
        out.println("<ul>");
        out.println("<li><a href='/public/rest/buildings'>Buildings</a></li>");
        out.println("<li><a href='/public/rest/apartments'>Apartments</a></li>");
        out.println("<li><a href='/public/rest/electricity'>Electricity</a></li>");
        out.println("<li><a href='/public/rest/watersupply'>Water</a></li>");
        out.println("<li><a href='/swagger-ui/index.html'>Swagger</a></li>");
        out.println("</ul>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}
