package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Map;

public abstract class Section implements Serializable {
    public static Section fromMap(String sectionType, Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Section map cannot be null for type: " + sectionType);
        }

        switch (sectionType) {
            case "PERSONAL":
            case "OBJECTIVE":
                Object contentObj = map.get("content");
                if (contentObj == null) {
                    throw new IllegalArgumentException("Content cannot be null for " + sectionType + " in map: " + map);
                }
                if (!(contentObj instanceof String)) {
                    throw new IllegalArgumentException("Content must be a String for " + sectionType + ", but was: " + contentObj.getClass().getName() + " in map: " + map);
                }
                return new TextSection((String) contentObj);

            case "ACHIEVEMENT":
            case "QUALIFICATIONS":
                Object itemsObj = map.get("items");
                if (itemsObj == null) {
                    throw new IllegalArgumentException("Items cannot be null for " + sectionType + " in map: " + map);
                }
                if (!(itemsObj instanceof java.util.List)) {
                    throw new IllegalArgumentException("Items must be a List for " + sectionType + ", but was: " + itemsObj.getClass().getName() + " in map: " + map);
                }
                @SuppressWarnings("unchecked")
                java.util.List<String> items = (java.util.List<String>) itemsObj;
                return new ListSection(items);

            case "EXPERIENCE":
            case "EDUCATION":
                return OrganizationSection.fromMap(map);

            default:
                throw new IllegalArgumentException("Unknown section type: " + sectionType + " in map: " + map);
        }
    }
}