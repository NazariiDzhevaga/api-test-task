package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyHandler {
    private final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);

    public Properties loadPropertiesFile(String filePath) {
        Properties props = new Properties();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            props.load(Objects.requireNonNull(resourceAsStream));
        } catch (IOException e) {
            logger.error("Unable to load properties file: {}", filePath);
        }
        return props;
    }
}
