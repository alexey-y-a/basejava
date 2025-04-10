package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    protected static final Resume RESUME_1 = new Resume(UUID_1, "Name1");
    protected static final Resume RESUME_2 = new Resume(UUID_2, "Name2");
    protected static final Resume RESUME_3 = new Resume(UUID_3, "Name3");
    protected static final Resume RESUME_4 = new Resume(UUID_4, "Name4");

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    protected void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertTrue(storage.getAllSorted().isEmpty());
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "Name1 Updated");
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
    }

    @Test
    public void get() {
        assertEquals(RESUME_1, storage.get(UUID_1));
        assertEquals(RESUME_2, storage.get(UUID_2));
        assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertEquals(RESUME_4, storage.get(UUID_4));
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                storage.update(new Resume("dummy", "Dummy Name")));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> result = storage.getAllSorted();
        assertEquals(3, result.size());
        assertEquals(Arrays.asList(RESUME_1, RESUME_2, RESUME_3), result);
    }
}