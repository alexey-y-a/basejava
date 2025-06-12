package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    protected static final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, "Name1");
    protected static final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, "Name2");
    protected static final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, "Name3");
    protected static final Resume RESUME_4 = ResumeTestData.createResume(UUID_4, "Name4");

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
        Resume newResume = ResumeTestData.createResume(UUID_1, "Name1 Updated");
        storage.update(newResume);
        Resume retrieved = storage.get(UUID_1);
        assertEquals(newResume.getUuid(), retrieved.getUuid());
        assertEquals(newResume.getFullName(), retrieved.getFullName());
    }

    @Test
    public void get() {
        Resume resume1 = storage.get(UUID_1);
        assertEquals(RESUME_1.getUuid(), resume1.getUuid());
        assertEquals(RESUME_1.getFullName(), resume1.getFullName());
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        Resume retrieved = storage.get(UUID_4);
        assertEquals(RESUME_4.getUuid(), retrieved.getUuid());
        assertEquals(RESUME_4.getFullName(), retrieved.getFullName());
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                storage.update(ResumeTestData.createResume("dummy", "Dummy Name")));
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
        assertEquals(RESUME_1.getUuid(), result.get(0).getUuid());
        assertEquals(RESUME_1.getFullName(), result.get(0).getFullName());
        assertEquals(RESUME_2.getUuid(), result.get(1).getUuid());
        assertEquals(RESUME_2.getFullName(), result.get(1).getFullName());
        assertEquals(RESUME_3.getUuid(), result.get(2).getUuid());
        assertEquals(RESUME_3.getFullName(), result.get(2).getFullName());
    }

}