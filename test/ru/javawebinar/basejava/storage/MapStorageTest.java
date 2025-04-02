package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    @Override
    public void saveOverflow() {

        storage.clear();
        for (int i = 0; i < 10000; i++) {
            storage.save(new Resume("uuid" + i));
        }
        assertSize(10000);
    }
}