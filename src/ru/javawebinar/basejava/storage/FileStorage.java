package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;

public class FileStorage extends AbstractFileStorage {
    public FileStorage(File directory) {
        super(directory);
    }

    public FileStorage(File directory, AbstractFileStorage.SerializationStrategy strategy) {
        super(directory, strategy);
    }

    @Override
    protected void doWrite(Resume r, File file) throws IOException {
        strategy.write(r, file);
    }

    @Override
    protected Resume doRead(File file) throws IOException {
        return strategy.read(file);
    }
}
