package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("Хранилище переполнено. Невозможно сохранить резюме с uuid " + r.getUuid());
            return;
        }
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            System.out.println("Резюме с uuid " + r.getUuid() + " уже существует.");
            return;
        }
        insertResume(r, index);
        size++;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Резюме с uuid " + uuid + " не найдено.");
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            System.out.println("Резюме с uuid " + resume.getUuid() + " не найдено для обновления.");
            return;
        }
        storage[index] = resume;
        System.out.println("Резюме с uuid " + resume.getUuid() + " обновлено.");
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Резюме с uuid " + uuid + " не найдено.");
            return;
        }
        removeResume(index);
        storage[--size] = null;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);
    protected abstract void insertResume(Resume r, int index);
    protected abstract void removeResume(int index);
}