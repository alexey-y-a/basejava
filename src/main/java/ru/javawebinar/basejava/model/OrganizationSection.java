package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrganizationSection extends Section implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Organization> organizations;

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public String toString() {
        return String.join("\n\n", organizations.stream().map(Object::toString).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @SuppressWarnings("unchecked")
    public static OrganizationSection fromMap(Map<String, Object> map) {
        validateMap(map);

        List<Map<String, Object>> orgList = extractOrganizations(map);
        if (orgList == null || orgList.isEmpty()) {
            return new OrganizationSection(new ArrayList<>());
        }

        List<Organization> organizations = new ArrayList<>();
        for (Map<String, Object> orgMap : orgList) {
            Organization organization = parseOrganization(orgMap);
            if (organization != null) {
                organizations.add(organization);
            }
        }

        return new OrganizationSection(organizations);
    }

    private static void validateMap(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("OrganizationSection map cannot be null");
        }
    }

    private static List<Map<String, Object>> extractOrganizations(Map<String, Object> map) {
        Object organizationsObj = map.get("organizations");
        if (organizationsObj == null) {
            throw new IllegalArgumentException("Organizations cannot be null in map: " + map);
        }
        if (!(organizationsObj instanceof List)) {
            throw new IllegalArgumentException("Organizations must be a List, but was: " + organizationsObj.getClass().getName() + " in map: " + map);
        }
        return (List<Map<String, Object>>) organizationsObj;
    }

    private static Organization parseOrganization(Map<String, Object> orgMap) {
        Map<String, Object> homePageMap = extractHomePage(orgMap);
        if (homePageMap == null) return null;

        String name = extractName(homePageMap);
        String url = extractUrl(homePageMap);

        List<Organization.Position> positions = extractPositions(orgMap);
        if (positions == null || positions.isEmpty()) {
            return null;
        }

        return new Organization(name, url, positions);
    }

    private static Map<String, Object> extractHomePage(Map<String, Object> orgMap) {
        Object homePageObj = orgMap.get("homePage");
        if (homePageObj == null) {
            throw new IllegalArgumentException("homePage cannot be null in organization map: " + orgMap);
        }
        if (!(homePageObj instanceof Map)) {
            throw new IllegalArgumentException("homePage must be a Map, but was: " + homePageObj.getClass().getName() + " in map: " + orgMap);
        }
        return (Map<String, Object>) homePageObj;
    }

    private static String extractName(Map<String, Object> homePageMap) {
        Object nameObj = homePageMap.get("name");
        if (nameObj == null) {
            throw new IllegalArgumentException("homePage name cannot be null in map: " + homePageMap);
        }
        if (!(nameObj instanceof String)) {
            throw new IllegalArgumentException("homePage name must be a String, but was: " + nameObj.getClass().getName() + " in map: " + homePageMap);
        }
        return (String) nameObj;
    }

    private static String extractUrl(Map<String, Object> homePageMap) {
        Object urlObj = homePageMap.get("url");
        return urlObj != null ? (String) urlObj : null;
    }

    @SuppressWarnings("unchecked")
    private static List<Organization.Position> extractPositions(Map<String, Object> orgMap) {
        Object positionsObj = orgMap.get("positions");
        if (positionsObj == null) {
            throw new IllegalArgumentException("positions cannot be null in organization map: " + orgMap);
        }
        if (!(positionsObj instanceof List)) {
            throw new IllegalArgumentException("positions must be a List, but was: " + positionsObj.getClass().getName() + " in map: " + orgMap);
        }

        List<Map<String, Object>> positionsList = (List<Map<String, Object>>) positionsObj;
        List<Organization.Position> positions = new ArrayList<>();

        for (Map<String, Object> posMap : positionsList) {
            Organization.Position position = parsePosition(posMap);
            if (position != null) {
                positions.add(position);
            }
        }

        return positions;
    }

    @SuppressWarnings("unchecked")
    private static Organization.Position parsePosition(Map<String, Object> posMap) {
        Object startDateObj = posMap.get("startDate");
        if (startDateObj == null) {
            throw new IllegalArgumentException("startDate cannot be null in position map: " + posMap);
        }
        if (!(startDateObj instanceof List)) {
            throw new IllegalArgumentException("startDate must be a List, but was: " + startDateObj.getClass().getName() + " in map: " + posMap);
        }
        @SuppressWarnings("unchecked")
        List<Number> startDateList = (List<Number>) startDateObj;
        if (startDateList.size() != 3) {
            throw new IllegalArgumentException("startDate must contain exactly 3 numbers (year, month, day), but was: " + startDateList + " in map: " + posMap);
        }
        LocalDate startDate = LocalDate.of(
                startDateList.get(0).intValue(),
                startDateList.get(1).intValue(),
                startDateList.get(2).intValue()
        );

        Object endDateObj = posMap.get("endDate");
        if (endDateObj == null) {
            throw new IllegalArgumentException("endDate cannot be null in position map: " + posMap);
        }
        if (!(endDateObj instanceof List)) {
            throw new IllegalArgumentException("endDate must be a List, but was: " + endDateObj.getClass().getName() + " in map: " + posMap);
        }
        @SuppressWarnings("unchecked")
        List<Number> endDateList = (List<Number>) endDateObj;
        if (endDateList.size() != 3) {
            throw new IllegalArgumentException("endDate must contain exactly 3 numbers (year, month, day), but was: " + endDateList + " in map: " + posMap);
        }
        LocalDate endDate = LocalDate.of(
                endDateList.get(0).intValue(),
                endDateList.get(1).intValue(),
                endDateList.get(2).intValue()
        );

        Object titleObj = posMap.get("title");
        if (titleObj == null) {
            throw new IllegalArgumentException("title cannot be null in position map: " + posMap);
        }
        if (!(titleObj instanceof String)) {
            throw new IllegalArgumentException("title must be a String, but was: " + titleObj.getClass().getName() + " in map: " + posMap);
        }
        String title = (String) titleObj;

        Object descriptionObj = posMap.get("description");
        String description = descriptionObj != null ? (String) descriptionObj : null;

        return new Organization.Position(startDate, endDate, title, description);
    }
}
