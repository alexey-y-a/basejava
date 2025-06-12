package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;

public interface SerializationStrategy {
    void write(Resume r, File file) throws IOException;
    Resume read(File file) throws IOException;
}
