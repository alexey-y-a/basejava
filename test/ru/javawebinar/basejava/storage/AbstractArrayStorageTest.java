package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = storage.size(); i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("uuid" + i, "Name" + i));
            }
        } catch (Exception e) {
            fail("Переполнение произошло раньше времени: " + e.getMessage());
        }
        StorageException thrown = org.junit.jupiter.api.Assertions.assertThrows(StorageException.class, () -> {
            storage.save(new Resume("overflow", "Overflow Name"));
        });
        assertNotNull(thrown);
    }
}