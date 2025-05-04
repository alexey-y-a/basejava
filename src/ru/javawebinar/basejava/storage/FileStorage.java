package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;

public class FileStorage extends AbstractFileStorage {
    public FileStorage(File directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Resume r, File file) throws IOException {
        super.doWrite(r, file);
    }

    @Override
    protected Resume doRead(File file) throws IOException {
        return super.doRead(file);
    }
}
