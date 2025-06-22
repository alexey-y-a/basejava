package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.NotExistStorageException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlStorageUtil {

    public static void executeUpdate(PreparedStatement ps, String uuid) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException("No rows affected for uuid: " + uuid);
        }
    }

    public static <T> T executeSingleResult(PreparedStatement ps, ResultSetExecutor<T> executor) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                throw new NotExistStorageException("No data found");
            }
            return executor.execute(rs);
        }
    }

    @FunctionalInterface
    public interface ResultSetExecutor<T> {
        T execute(ResultSet rs) throws SQLException;
    }

    public static <T> T executeListResult(PreparedStatement ps, ResultSetExecutor<T> executor) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            return executor.execute(rs);
        }
    }

}
