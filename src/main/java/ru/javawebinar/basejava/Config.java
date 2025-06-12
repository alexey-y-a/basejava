package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.Storage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String PROPS = "config/resumes.properties";
    private static final Config INSTANCE = new Config();
    private final Properties props = new Properties();
    private Storage storage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPS)) {
            if (is == null) {
                throw new IllegalStateException("Config file " + PROPS + " not found in classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Error loading config file " + PROPS, e);
        }
        this.storage = new ru.javawebinar.basejava.storage.SqlStorage(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public Storage getStorage() {
        return storage;
    }

}
