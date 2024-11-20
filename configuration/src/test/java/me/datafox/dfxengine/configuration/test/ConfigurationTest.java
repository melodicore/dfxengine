package me.datafox.dfxengine.configuration.test;

import me.datafox.dfxengine.configuration.ConfigurationImpl;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.api.annotation.Component;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class ConfigurationTest {
    private static final ConfigurationKey<String> TEST_1 = ConfigurationKey.of("default string");

    private static ConfigurationManager manager;

    @BeforeAll
    public static void beforeAll() {
        InjectorBuilder.scan();
        manager = new InjectorBuilder().build().getComponent(ConfigurationManager.class);
    }

    @Test
    public void test() {
        Configuration configuration = manager.getConfiguration();

        assertEquals("overridden", configuration.get(TEST_1));
        configuration.clear(TEST_1);
        assertEquals("default string", configuration.get(TEST_1));
    }

    @Component
    public static Configuration configurationComponent() {
        return ConfigurationImpl.builder().key(TEST_1, "overridden").build();
    }
}
