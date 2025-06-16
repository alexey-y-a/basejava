package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final SimpleConnectionFactory connectionFactory;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public SqlHelper(SimpleConnectionFactory connectionFactory, String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = connectionFactory;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void execute(String sql) {
        execute(sql, PreparedStatement::execute);
    }

    public <T> T execute(String sql, SqlExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        Connection conn = null;
        try {
            conn = connectionFactory.getConnection();
            conn.setAutoCommit(false);
            T res = executor.execute(conn);
            conn.commit();
            return res;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.rollback();
                        System.out.println("Rollback executed for connection: " + conn);
                    } else {
                        System.out.println("Connection is already closed during rollback attempt: " + conn);
                    }
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                    throw new StorageException("Rollback failed", ex);
                }
            }
            throw ExceptionUtil.convertException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connection closed: " + conn);
                } catch (SQLException e) {
                    throw new StorageException("Failed to close connection", e);
                }
            }
        }
    }

}
