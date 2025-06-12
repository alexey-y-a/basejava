package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.DataStreamSerializer;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileStorageTest extends  AbstractStorageTest {
    private static final File STORAGE_DIR = new File("file_storage");

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new DataStreamSerializer()));
    }

    @BeforeEach
    public void setUp() {
        if (STORAGE_DIR.exists()) {
            for (File file : STORAGE_DIR.listFiles()) {
                file.delete();
            }
            STORAGE_DIR.delete();
        }
        STORAGE_DIR.mkdirs();
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void testSaveAndGetWithDataStream() {
        assertEquals(RESUME_1, storage.get(UUID_1));
        assertEquals(RESUME_2, storage.get(UUID_2));
        assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void testUpdateWithDataStream() {
        Resume updatedResume = ResumeTestData.createResume(UUID_1, "Name1 Updated");
        storage.update(updatedResume);
        assertEquals(updatedResume, storage.get(UUID_1));
    }

    @Test
    public void testDeleteWithDataStream() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(StorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void testReadNonExistentFileWithDataStream() {
        StorageException thrown = assertThrows(StorageException.class, () -> storage.get("non_existent_uuid"));
        assertNotNull(thrown, "Должно быть выброшено исключение при чтении несуществующего файла");
    }
}
