package me.datafox.dfxengine.entities.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author datafox
 */
public class TestLoader {
    public static String load(String path) {
        try {
            return Files.readString(Path.of(Objects.requireNonNull(TestLoader.class.getResource(path)).toURI()));
        } catch(IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
