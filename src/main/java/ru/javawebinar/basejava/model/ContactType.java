package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Телефон"),
    MOBILE("Мобильный"),
    HOME_PHONE("Домашний телефон"),
    SKYPE("Skype"),
    MAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        if (value == null) return "";
        switch (this) {
            case PHONE:
            case MOBILE:
            case HOME_PHONE:
                return "<a href='tel:" + value.replaceAll("[\\s-]", "") + "'>" + value + "</a>";
            case MAIL:
                return "<a href='mailto:" + value + "'>" + value + "</a>";
            case SKYPE:
                return "<a href='skype:" + value + "?call'>" + value + "</a>";
            case LINKEDIN:
                return "<a href='" + (value.startsWith("http") ? value : "https://www.linkedin.com/in/" + value) + "' target='_blank'>" + value + "</a>";
            case GITHUB:
                return "<a href='" + (value.startsWith("http") ? value : "https://github.com/" + value) + "' target='_blank'>" + value + "</a>";
            case STACKOVERFLOW:
                return "<a href='" + (value.startsWith("http") ? value : "https://stackoverflow.com/users/" + value) + "' target='_blank'>" + value + "</a>";
            case HOME_PAGE:
                return "<a href='" + (value.startsWith("http") ? value : "http://" + value) + "' target='_blank'>" + value + "</a>";
            default:
                return value;
        }
    }
}