package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.text.api.NameConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author datafox
 */
public class NameConverterTest extends AbstractTest {
    private Handle testHandle;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        testHandle = injector.getComponent(HandleManager.class).getOrCreateSpace("test").getOrCreateHandle("test");
    }

    @Test
    public void handledNameConverterTest() {
        NameConverter<Handled> converter = factory.getNameConverter(Handled.class);
        assertEquals("test", converter.convert(() -> testHandle));
        assertEquals("null", converter.convert(null));
        assertNull(converter.convertPlural(() -> testHandle));
    }
    @Test
    public void handleNameConverterTest() {
        NameConverter<Handle> converter = factory.getNameConverter(Handle.class);
        assertEquals("test", converter.convert(testHandle));
        assertEquals("null", converter.convert(null));
        assertNull(converter.convertPlural(testHandle));
    }
}
