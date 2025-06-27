package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getStorage();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
            return;
        }

        Resume r;
        switch (action) {
            case "view":
                r = storage.get(uuid);
                request.setAttribute("resume", r);
                request.getRequestDispatcher("/WEB-INF/views/view.jsp").forward(request, response);
                break;
            case "edit":
                r = storage.get(uuid);
                request.setAttribute("resume", r);
                request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);
                break;
            case "add":
                r = new Resume("", "");
                request.setAttribute("resume", r);
                request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                break;
            default:
                response.sendRedirect("resume");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName").trim();

        if (fullName.isEmpty()) {
            Resume r = uuid == null || uuid.trim().isEmpty() ? new Resume("", "") : storage.get(uuid);
            request.setAttribute("resume", r);
            request.setAttribute("error", "Имя не может быть пустым!");
            request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);
            return;
        }

        Resume oldResume = uuid != null && !uuid.trim().isEmpty() ? storage.get(uuid) : null;
        Resume r = new Resume(uuid == null || uuid.trim().isEmpty() ? "" : uuid, fullName);
        if (oldResume != null) {
            for (Map.Entry<ContactType, String> entry : oldResume.getContacts().entrySet()) {
                if (request.getParameter(entry.getKey().name()) == null) {
                    r.setContact(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<SectionType, Section> entry : oldResume.getSections().entrySet()) {
                if (request.getParameter(entry.getKey().name()) == null) {
                    r.setSection(entry.getKey(), entry.getValue());
                }
            }
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.setContact(type, value.trim());
            }
        }

        for (SectionType type : SectionType.values()) {
            String content = request.getParameter(type.name());
            if (content != null && !content.trim().isEmpty()) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        r.setSection(type, new TextSection(content.trim()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = new ArrayList<>();
                        for (String item : content.split("\n")) {
                            String trimmedItem = item.trim();
                            if (!trimmedItem.isEmpty()) {
                                items.add(trimmedItem);
                            }
                        }
                        if (!items.isEmpty()) {
                            r.setSection(type, new ListSection(items));
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        break;
                }
            }
        }

        if (uuid == null || uuid.trim().isEmpty()) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }


}