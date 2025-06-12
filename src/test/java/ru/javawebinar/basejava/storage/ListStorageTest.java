package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListStorageTest extends AbstractStorageTest {
    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    void testSpecificListStorage() {
        assertEquals(3, storage.size());
    }
}