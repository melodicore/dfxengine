package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.formatter.SplittingNumberFormatter;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.TextHandles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

/**
 * @author datafox
 */
public class AbstractTest {
    protected Injector injector;
    protected TextFactory factory;
    protected TextHandles handles;

    @BeforeAll
    public static void beforeAll() {
        InjectorBuilder.scan();
    }

    @BeforeEach
    public void beforeEach() {
        injector = new InjectorBuilder().build();
        factory = injector.getComponent(TextFactory.class);
        handles = injector.getComponent(TextHandles.class);
    }

    @AfterEach
    public void afterEach() throws NoSuchFieldException, IllegalAccessException {
        Field field = ConfigurationKey.class.getDeclaredField("defaultValue");
        field.trySetAccessible();
        field.set(ConfigurationKeys.NUMBER_FORMATTER, null);
        field.set(ConfigurationKeys.NUMBER_SUFFIX_FORMATTER, null);
        field.set(SplittingNumberFormatter.FORMATTER, null);
    }
}
