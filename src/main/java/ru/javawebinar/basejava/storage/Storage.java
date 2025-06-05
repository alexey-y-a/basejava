package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public interface Storage {
    void clear();
    void save(Resume r);
    Resume get(String uuid);
    void update(Resume resume);
    void delete(String uuid);
    List<Resume> getAllSorted();
    int size();
}