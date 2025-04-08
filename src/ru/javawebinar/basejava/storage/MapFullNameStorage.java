package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFullNameStorage extends AbstractStorage {
    private final Map<String, Resume> map = new HashMap<>();

    @Override
    protected Object getSearchKey(Resume r) {
        return map.containsKey(r.getUuid()) ? r.getUuid() : null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null && map.containsKey((String) searchKey);
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        map.put((String) searchKey, r);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        for (Resume resume : map.values()) {
            if (resume.getFullName().equals(r.getFullName())) {
                throw new ExistStorageException(r.getUuid());
            }
        }
        map.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return map.get((String) searchKey);
    }

    @Override
    protected void doDelete(Object searchKey) {
        map.remove((String) searchKey);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return list;
    }

    @Override
    public int size() {
        return map.size();
    }
}