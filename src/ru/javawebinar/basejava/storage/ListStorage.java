package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> list = new ArrayList<>();

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doUpdate(Resume r, Object index) {
        list.set((Integer) index, r);
    }

    @Override
    protected void doSave(Resume r, Object index) {
        list.add(r);
    }

    @Override
    protected Resume doGet(Object index) {
        return list.get((Integer) index);
    }

    @Override
    protected void doDelete(Object index) {
        list.remove((int) index);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(list);
    }

    @Override
    public int size() {
        return list.size();
    }
}