package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.ObjectStreamSerialization;
import ru.javawebinar.basejava.storage.serializer.SerializationStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final SerializationStrategy strategy;

    protected FileStorage(File directory) {
        this(directory, new ObjectStreamSerialization());
    }

    protected FileStorage(File directory, SerializationStrategy strategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(strategy, "strategy must not be null");
        this.directory = directory;
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new StorageException("Failed to create directory " + directory.getAbsolutePath());
            }
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.strategy = strategy;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File searchKey) {
        return (searchKey).exists();
    }

    @Override
    protected void doUpdate(Resume r, File searchKey) {
        try {
            strategy.write(r, searchKey);
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File searchKey) {
        try {
            if (!searchKey.createNewFile()) {
                throw new StorageException("File already exists", searchKey.getName());
            }
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + searchKey.getAbsolutePath(), searchKey.getName(), e);
        }
        doUpdate(r, searchKey);
    }

    @Override
    protected Resume doGet(File searchKey) {
        try {
            return strategy.read(searchKey);
        } catch (IOException e) {
            throw new StorageException("File read error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doDelete(File searchKey) {
        if (!searchKey.delete()) {
            throw new StorageException("File delete error", searchKey.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = getFilesWithErrorHandling();
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(doGet(file));
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = getFilesWithErrorHandling();
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        File[] files = getFilesWithErrorHandling();
        return files.length;
    }

    private File[] getFiles() {
        return directory.listFiles();
    }

    private File[] getFilesWithErrorHandling() {
        File[] files = getFiles();
        if (files == null) {
            throw new StorageException("Directory read error");
        }
        return files;
    }

}
