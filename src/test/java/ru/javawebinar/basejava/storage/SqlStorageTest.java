package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlStorageTest extends AbstractStorageTest {

    private static final Config CONFIG = Config.get();

    public SqlStorageTest() {
        super(new SqlStorage(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password")
        ));
    }

    @Test
    void testSpecificSqlStorage() {
        Resume testResume = new Resume(UUID.randomUUID().toString(), "Test Name");
        storage.save(testResume);
        assertEquals(4, storage.size());
    }
}
