package com.petfeet.util;

import com.petfeet.exception.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central place that opens JDBC connections using DriverManager.
 * Settings are read once from db.properties (classpath) and can be overridden with
 * the system properties -Ddb.url, -Ddb.user and -Ddb.password.
 */
public final class DBConnection {
    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                CONFIG.load(in);
            }
            Class.forName(System.getProperty("db.driver", CONFIG.getProperty("db.driver", "com.mysql.cj.jdbc.Driver")));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Cannot initialise database settings: " + e.getMessage());
        }
    }

    private DBConnection() { }

    private static String setting(String key) {
        return System.getProperty(key, CONFIG.getProperty(key));
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(setting("db.url"), setting("db.user"), setting("db.password"));
        } catch (SQLException e) {
            throw new DatabaseException("Could not connect to the database. Please check db.properties.", e);
        }
    }
}
