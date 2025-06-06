package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String url;

    public Link(String name, String url) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return name + (url != null ? " (" + url + ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(name, link.name) &&
                Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}