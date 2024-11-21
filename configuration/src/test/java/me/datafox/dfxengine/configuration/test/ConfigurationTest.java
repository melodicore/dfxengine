package me.datafox.dfxengine.configuration.test;

import me.datafox.dfxengine.configuration.ConfigurationImpl;
import me.datafox.dfxengine.configuration.InitialConfiguration;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class ConfigurationTest {
    private static final ConfigurationKey<String> TEST_1 = ConfigurationKey.of("default string");

    private static Injector injector;

    private static ConfigurationManager manager;

    private static InitialTester tester;

    @BeforeAll
    public static void beforeAll() {
        InjectorBuilder.scan();
        injector = new InjectorBuilder().build();
        manager = injector.getComponent(ConfigurationManager.class);
        tester = injector.getComponent(InitialTester.class);
    }

    @Test
    public void test() {
        assertEquals("initial", tester.read);
        assertEquals("overridden", manager.get(TEST_1));
        manager.clear(TEST_1);
        assertEquals("default string", manager.get(TEST_1));
    }

    @Component
    public static ConfigurationImpl configurationComponent() {
        return new ConfigurationImpl().set(TEST_1, "overridden");
    }

    @Component(order = -1)
    public static InitialConfiguration initialConfigurationComponent() {
        return new InitialConfiguration().set(TEST_1, "initial");
    }

    @Component
    public static class InitialTester {
        public String read;

        @Inject
        public InitialTester(ConfigurationManager manager) {
            read = manager.get(TEST_1);
        }
    }
}
