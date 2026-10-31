package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.DaoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Reads db.url / db.user / db.password from medipass.properties (classpath). */
public final class ConnectionManager {

    private static final Properties PROPS = load();

    private ConnectionManager() { }

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = ConnectionManager.class.getResourceAsStream("/medipass.properties")) {
            if (in != null) p.load(in);
        } catch (IOException e) {
            throw new DaoException("Cannot read medipass.properties", e);
        }
        return p;
    }

    public static String property(String key, String fallback) {
        return System.getProperty(key, PROPS.getProperty(key, fallback));
    }

    /** A new connection; callers close it with try-with-resources. */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    property("db.url", "jdbc:mysql://localhost:3306/medipass"),
                    property("db.user", "root"),
                    property("db.password", ""));
        } catch (SQLException e) {
            throw new DaoException("Database connection failed: " + e.getMessage(), e);
        }
    }
}
