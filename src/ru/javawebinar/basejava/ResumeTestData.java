package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.Arrays;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("11111111-1111-1111-1111-111111111111", "Алексей Яковлев");

        resume.setContact(ContactType.PHONE, "+7(---) --------");
        resume.setContact(ContactType.SKYPE, "skype:skype");
        resume.setContact(ContactType.MAIL, "@mail.ru");

        resume.setSection(SectionType.OBJECTIVE, new TextSection("тестировщик ПО"));

        resume.setSection(SectionType.PERSONAL, new TextSection("Опыт руководства персоналом, организованность"));

        resume.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(
                "Тестирование сетевого ПО, анализ и устранение проблем в продукте"
        )));

        resume.setSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(
                "Version control: Git",
                "DB: PostgreSQL, Oracle, MySQL, SQL",
                "Родной русский, английский \"Intermediate\""
        )));

        resume.setSection(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(
                new Organization("OOO", null, LocalDate.of(2023, 9, 6), LocalDate.now(), "Инженер по тестированию ПО", null),
                new Organization("OOO", null, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 9, 5), "Системный администратор", null),
                new Organization("OOO", null, LocalDate.of(2020, 6, 1), LocalDate.of(2022, 12, 12), "Начальник отдела технического-контроля", null)
        )));

        resume.setSection(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(
                new Organization("Российский университет дружбы народов", null, LocalDate.of(2012, 9, 1), LocalDate.of(2014, 7, 1), "Магистр (инженер)", null),
                new Organization("Российский университет дружбы народов", null, LocalDate.of(2008, 9, 1), LocalDate.of(2012, 7, 1), "Бакалавр (инженер)", null)
        )));

        System.out.println("Резюме: " + resume.getFullName());
        System.out.println("Контакты:");
        for (ContactType type : ContactType.values()) {
            String contact = resume.getContact(type);
            if (contact != null) {
                System.out.println(type.getTitle() + ": " + contact);
            }
        }
        System.out.println("\nСекции:");
        for (SectionType type : SectionType.values()) {
            Section section = resume.getSection(type);
            if (section != null) {
                System.out.println(type.getTitle() + ":");
                System.out.println(section);
            }
        }
    }
}