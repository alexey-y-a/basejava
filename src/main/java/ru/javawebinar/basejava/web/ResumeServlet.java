package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    OrganizationSection section = (OrganizationSection) r.getSection(type);
                    List<Organization> orgs = new ArrayList<>();
                    orgs.add(new Organization("", "", new ArrayList<>()));
                    if (section != null) {
                        orgs.addAll(section.getOrganizations());
                    }
                    r.setSection(type, new OrganizationSection(orgs));
                }
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

        Resume resume = uuid == null || uuid.trim().isEmpty() ? new Resume("", fullName) : storage.get(uuid);

        if (uuid != null && !uuid.trim().isEmpty()) {
            for (Map.Entry<ContactType, String> entry : storage.get(uuid).getContacts().entrySet()) {
                if (request.getParameter(entry.getKey().name()) == null) {
                    resume.setContact(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<SectionType, Section> entry : storage.get(uuid).getSections().entrySet()) {
                if (request.getParameter(entry.getKey().name()) == null) {
                    resume.setSection(entry.getKey(), entry.getValue());
                }
            }
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                resume.setContact(type, value.trim());
            } else {
                resume.setContact(type, null);
            }
        }

        for (SectionType type : SectionType.values()) {
            String content = request.getParameter(type.name());
            if (content != null && !content.trim().isEmpty()) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.setSection(type, new TextSection(content.trim()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = new ArrayList<>();
                        for (String item : content.split("\\r?\\n")) {
                            String trimmedItem = item.trim();
                            if (!trimmedItem.isEmpty()) {
                                items.add(trimmedItem);
                            }
                        }
                        if (!items.isEmpty()) {
                            resume.setSection(type, new ListSection(items));
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = new ArrayList<>();
                        int orgIndex = 0;
                        String orgName;
                        while ((orgName = request.getParameter(type.name() + orgIndex + ".name")) != null && !orgName.trim().isEmpty()) {
                            String orgUrl = request.getParameter(type.name() + orgIndex + ".url");
                            List<Organization.Position> positions = new ArrayList<>();
                            int posIndex = 0;
                            String posTitle;
                            while ((posTitle = request.getParameter(type.name() + orgIndex + ".position" + posIndex + ".title")) != null && !posTitle.trim().isEmpty()) {
                                String startDateStr = request.getParameter(type.name() + orgIndex + ".position" + posIndex + ".startDate");
                                String endDateStr = request.getParameter(type.name() + orgIndex + ".position" + posIndex + ".endDate");
                                String description = request.getParameter(type.name() + orgIndex + ".position" + posIndex + ".description");
                                LocalDate startDate = parseDate(startDateStr);
                                LocalDate endDate = parseDate(endDateStr);
                                if (startDate != null && endDate != null) {
                                    positions.add(new Organization.Position(startDate, endDate, posTitle, description));
                                }
                                posIndex++;
                            }
                            if (!positions.isEmpty()) {
                                organizations.add(new Organization(orgName.trim(), orgUrl, positions));
                            }
                            orgIndex++;
                        }
                        if (!organizations.isEmpty()) {
                            resume.setSection(type, new OrganizationSection(organizations));
                        }
                        break;
                }
            } else {
                resume.setSection(type, null);
            }
        }

        if (uuid == null || uuid.trim().isEmpty()) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            return LocalDate.parse(dateStr + "/01", formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
