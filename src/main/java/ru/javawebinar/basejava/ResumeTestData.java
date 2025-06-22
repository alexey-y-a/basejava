package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.util.Arrays;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = createResume("11111111-1111-1111-1111-111111111111", "Алексей Яковлев");
        printResume(resume);
    }

    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        resume.setContact(ContactType.PHONE, "+7(---) --------");
        resume.setContact(ContactType.SKYPE, "skype:skype");
        resume.setContact(ContactType.MAIL, "@mail.ru");

        resume.setSection(SectionType.OBJECTIVE, new TextSection("тестировщик ПО"));

        resume.setSection(SectionType.PERSONAL, new TextSection(
                "обучение и развитие, стрессоустойчивость, организованность"));

        resume.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(
                "Тестирование сетевого ПО, анализ и устранение проблем в продукте"
        )));

        resume.setSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(
                "Version control: Git",
                "DB: PostgreSQL, Oracle, MySQL, SQL",
                "Родной русский, английский \"Intermediate\""
        )));

        return resume;
    }

    private static void printResume(Resume resume) {
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