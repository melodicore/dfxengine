package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerConfiguration;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleManagerTest {
    private HandleManager hm;

    @BeforeEach
    public void beforeEach() {
        hm = new HandleManagerImpl(LoggerFactory.getLogger(HandleManagerImpl.class), HandleManagerConfiguration.defaultConfiguration());
    }

    @Test
    public void createSpaceTest() {
        Space space1 = assertDoesNotThrow(() -> hm.createSpace("space1"));
        assertDoesNotThrow(() -> hm.createSpace("space 2!"));
        assertThrows(IllegalArgumentException.class, () -> hm.createSpace("space1"));
        Space _space1 = assertDoesNotThrow(() -> hm.getOrCreateSpace("space1"));
        assertSame(space1, _space1);
    }

    @Test
    public void spaceSpaceTest() {
        Space space = hm.getSpaceSpace();
        Handle handle = hm.createSpace("space").getHandle();
        assertNotNull(space.getHandles().get("space"));
        assertThrows(UnsupportedOperationException.class, () -> space.createHandle("yeeboi"));
        Handle _handle = assertDoesNotThrow(() -> space.getOrCreateHandle("space"));
        assertThrows(UnsupportedOperationException.class, () -> space.getOrCreateHandle("aight"));
        assertSame(handle, _handle);
    }

    @Test
    public void spaceHandleTest() {
        Space space = hm.createSpace("space");
        Handle shandle = space.createGroup("group").getHandle();
        Handle handle = space.getHandle();
        assertThrows(UnsupportedOperationException.class, () -> handle.createSubHandle("sub"));
        Handle _shandle = assertDoesNotThrow(() -> handle.getOrCreateSubHandle("group"));
        assertThrows(UnsupportedOperationException.class, () -> handle.getOrCreateSubHandle("dom"));
        assertSame(shandle, _shandle);
    }
}
