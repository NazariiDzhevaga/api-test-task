package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Config {
    public static final Logger log = LoggerFactory.getLogger(Config.class);
    public static final String CONFIG_PROPERTIES = "config.properties";
    private static Properties properties;

    public static void initialize() {
        properties = new PropertyHandler().loadPropertiesFile(CONFIG_PROPERTIES);

        for (String key : properties.stringPropertyNames()) {
            if (System.getProperties().containsKey(key)) {
                properties.setProperty(key, System.getProperty(key));
            }
        }

        log.info("Test Properties");
        log.info("====================================");
        for (String key : properties.stringPropertyNames()) {
            log.info("{}={}", key, properties.getProperty(key));
        }
        log.info("====================================");
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
