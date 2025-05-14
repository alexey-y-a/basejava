package ru.javawebinar.basejava.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message) {
        super(message);
        this.uuid = null;
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid, Throwable cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
        this.uuid = null;
    }

    public String getUuid() {
        return uuid;
    }
}