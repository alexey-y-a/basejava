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
    protected final SerializationStrategy strategy;

    protected FileStorage(File directory) {
        this(directory, new ObjectStreamSerialization());
    }

    protected FileStorage(File directory, SerializationStrategy strategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(strategy, "strategy must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
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

    protected void doWrite(Resume r, File file) throws IOException {
        strategy.write(r, file);
    }

    protected Resume doRead(File file) throws IOException {
        return strategy.read(file);
    }

    @Override
    protected void doUpdate(Resume r, File searchKey) {
        try {
            doWrite(r, searchKey);
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
            doWrite(r, searchKey);
        } catch (IOException e) {
            throw new StorageException("File write error ", r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(File searchKey) {
        try {
            return doRead(searchKey);
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
        File[] files = getFiles();
        if (files == null) {
            throw new StorageException("Directory read error");
        }
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(doGet(file));
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = getFiles();
        if (files == null) {
            throw new StorageException("Directory read error");
        }
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        File[] files = getFiles();
        if (files == null) {
            throw new StorageException("Directory read error");
        }
        return files.length;
    }

    private File[] getFiles() {
        return directory.listFiles();
    }

}
