package ru.javawebinar.basejava.storage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test
    public void testDelete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        assertNull(storage.get(UUID_1));
    }
}