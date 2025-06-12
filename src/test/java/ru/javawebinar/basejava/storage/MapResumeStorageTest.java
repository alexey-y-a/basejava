package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapResumeStorageTest extends AbstractStorageTest {

    public MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Test
    void testSpecificMapResumeStorage() {
        assertEquals(3, storage.size());
    }
}