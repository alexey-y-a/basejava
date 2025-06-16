package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final SqlStorage storage = new SqlStorage("jdbc:postgresql://localhost:5432/resumes", "alexey", "");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String name = request.getParameter("name");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("    <meta charset=\"UTF-8\">\n")
                .append("    <link rel=\"stylesheet\" href=\"css/style.css\">\n")
                .append("    <title>Курс JavaSE + Web</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("    <header>Приложение вебинара <a href=\"http://javawebinar.ru/basejava/\" target=\"_blank\">Практика Java. Разработка Web приложения.\"</a></header>\n")
                .append("    <h1>Курс JavaSE + Web</h1>\n")
                .append("    <table border=\"1\">\n")
                .append("        <tr>\n")
                .append("            <th>UUID</th>\n")
                .append("            <th>Full Name</th>\n")
                .append("        </tr>\n");

        List<Resume> resumes = storage.getAllSorted();
        for (Resume r : resumes) {
            html.append(String.format(
                    "        <tr>\n" +
                            "            <td>%s</td>\n" +
                            "            <td>%s</td>\n" +
                            "        </tr>\n", r.getUuid(), r.getFullName()
            ));
        }

        html.append("    </table>\n")
                .append("    <footer>Приложение вебинара <a href=\"http://javawebinar.ru/basejava/\" target=\"_blank\">Практика Java. Разработка Web приложения.\"</a></footer>\n")
                .append("</body>\n")
                .append("</html>");

        response.getWriter().write(html.toString());
    }
}