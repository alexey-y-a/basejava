package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final Connection connection;

    public SqlHelper(Connection connection) {
        this.connection = connection;
        if (connection == null) {
            throw new StorageException("Connection is null");
        }
    }

    public <T> T execute(String sql, SqlExecutor<T> executor) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                throw new ExistStorageException("Duplicate UUID: " + e.getMessage(), e);
            }
            throw new StorageException("SQL error: " + e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface SqlExecutor<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

}
