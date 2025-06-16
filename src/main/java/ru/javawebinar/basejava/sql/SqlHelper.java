package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public <T> T execute(String sql, SqlExecutor<T> executor) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                throw new ExistStorageException("Duplicate UUID: " + e.getMessage(), e);
            }
            throw new StorageException("SQL error: " + e.getMessage(), e);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            conn.setAutoCommit(false);
            T res = executor.execute(conn);
            conn.commit();
            return res;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                    throw new StorageException("Rollback failed", ex);
                }
            }
            throw ExceptionUtil.convertException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new StorageException("Failed to close connection", e);
                }
            }
        }
    }

    @FunctionalInterface
    public interface SqlExecutor<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlTransaction<T> {
        T execute(Connection conn) throws SQLException;
    }

}
