package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getSearchKey(Resume r);
    protected abstract boolean isExist(Object searchKey);
    protected abstract void doUpdate(Resume r, Object searchKey);
    protected abstract void doSave(Resume r, Object searchKey);
    protected abstract Resume doGet(Object searchKey);
    protected abstract void doDelete(Object searchKey);

    @Override
    public abstract List<Resume> getAllSorted();

    @Override
    public void update(Resume r) {
        Object searchKey = getExistedSearchKey(r);
        doUpdate(r, searchKey);
    }

    @Override
    public void save(Resume r) {
        Object searchKey = getNotExistedSearchKey(r);
        doSave(r, searchKey);
    }

    @Override
    public void delete(String key) {
        Resume r = (this instanceof MapFullNameStorage) ? getResumeByFullName(key) : getResumeByUuid(key);
        if (r == null) {
            throw new NotExistStorageException(key);
        }
        Object searchKey = getExistedSearchKey(r);
        doDelete(searchKey);
    }

    @Override
    public Resume get(String key) {
        Object searchKey = getExistedSearchKeyForGet(key);
        return doGet(searchKey);
    }

    private Object getExistedSearchKey(Resume r) {
        Object searchKey = getSearchKey(r);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(r.getUuid());
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(Resume r) {
        Object searchKey = getSearchKey(r);
        if (isExist(searchKey)) {
            throw new ExistStorageException(r.getUuid());
        }
        return searchKey;
    }

    private Object getExistedSearchKeyForGet(String key) {
        if (this instanceof MapFullNameStorage) {
            for (Resume r : getAllSorted()) {
                if (r.getFullName().equals(key)) {
                    return getSearchKey(r);
                }
            }
        } else {
            for (Resume r : getAllSorted()) {
                if (r.getUuid().equals(key)) {
                    return getSearchKey(r);
                }
            }
        }
        throw new NotExistStorageException(key);
    }

    private Resume getResumeByUuid(String uuid) {
        for (Resume r : getAllSorted()) {
            if (r.getUuid().equals(uuid)) {
                return r;
            }
        }
        return null;
    }

    private Resume getResumeByFullName(String fullName) {
        for (Resume r : getAllSorted()) {
            if (r.getFullName().equals(fullName)) {
                return r;
            }
        }
        return null;
    }
}