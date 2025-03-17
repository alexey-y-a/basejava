package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size >= storage.length) {
            System.out.println("Хранилище переполнено. Невозможно сохранить резюме с uuid " + r.getUuid());
            return;
        }
        if (findIndex(r.getUuid()) != -1) {
            System.out.println("Резюме с uuid " + r.getUuid() + " уже существует.");
            return;
        }
        storage[size++] = r;
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == - 1) {
            System.out.println("Резюме с uuid " + uuid + " не найдено");
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index == -1) {
            System.out.println("Резюме с uuid " + resume.getUuid() + " не найдено для обновления.");
            return;
        }
        storage[index] = resume;
        System.out.println("Резюме с uuid " + resume.getUuid() + " обновлено.");
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("Резюме с uuid " + uuid + " не найдено.");
            return;
        }
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        storage[--size] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
