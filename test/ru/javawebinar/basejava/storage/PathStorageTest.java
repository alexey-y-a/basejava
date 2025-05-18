package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerialization;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PathStorageTest extends AbstractStorageTest {
    private static final File STORAGE_DIR = new File("path_storage");

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerialization()));
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void testObjectStreamSerialization() {
        Resume savedResume = storage.get(UUID_1);
        assertEquals(RESUME_1, savedResume, "Сохранённое резюме не совпадает с исходным");
    }

    @Test
    public void testReadNonExistentFile() {
        StorageException thrown = assertThrows(StorageException.class, () -> {
            storage.get("non_existent_uuid");
        });
        assertNotNull(thrown, "Должно быть выброшено исключение при чтении несуществующего файла");
    }
}
