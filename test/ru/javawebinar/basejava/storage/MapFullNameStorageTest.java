package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

public class MapFullNameStorageTest extends AbstractStorageTest {

    public MapFullNameStorageTest() {
        super(new MapFullNameStorage());
    }

    @Override
    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "Name1 Updated");
        storage.update(newResume);
        assertSame(newResume, storage.get("Name1 Updated"));
    }

    @Override
    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy", "Dummy Name")));
    }

    @Override
    @Test
    public void delete() {
        storage.delete("Name1");
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get("Name1"));
    }

    @Override
    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("Dummy Name"));
    }

    @Override
    @Test
    public void get() {
        assertEquals(RESUME_1, storage.get("Name1"));
        assertEquals(RESUME_2, storage.get("Name2"));
        assertEquals(RESUME_3, storage.get("Name3"));
    }

    @Override
    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("Dummy Name"));
    }
}