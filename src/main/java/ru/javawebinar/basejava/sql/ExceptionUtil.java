package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {

    public static StorageException convertException(SQLException e) {
        if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
            return new ExistStorageException("Duplicate UUID", e);
        }
        return new StorageException("SQL error", e);
    }

}
