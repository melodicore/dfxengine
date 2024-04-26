package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerConfiguration;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleTest {
    private Space space;
    private Handle handle;

    @BeforeEach
    public void beforeEach() {
        HandleManager hm = new HandleManagerImpl(LoggerFactory.getLogger(HandleManagerImpl.class),
                HandleManagerConfiguration.defaultConfiguration());
        space = hm.createSpace("space");
        handle = space.createHandle("handle");
    }

    @Test
    public void handleIdTest() {
        assertThrows(NullPointerException.class, () -> space.createHandle(null));
        assertThrows(IllegalArgumentException.class, () -> space.createHandle(""));
        assertThrows(IllegalArgumentException.class, () -> space.createHandle("  "));
        assertThrows(IllegalArgumentException.class, () -> space.createHandle("äö"));

    }

    @Test
    public void subHandleTest() {
        Handle sub1 = assertDoesNotThrow(() -> handle.createSubHandle("sub"));
        Handle sub2 = assertDoesNotThrow(() -> handle.createSubHandle("handle"));
        assertThrows(IllegalArgumentException.class, () -> handle.createSubHandle("sub"));
        assertEquals("handle:sub", sub1.getId());
        assertEquals("handle:handle", sub2.getId());
        assertEquals(0, handle.getIndex());
        assertEquals(0, sub1.getIndex());
        assertEquals(0, sub2.getIndex());
        assertEquals(-1, handle.getSubIndex());
        assertEquals(0, sub1.getSubIndex());
        assertEquals(1, sub2.getSubIndex());
        List<Handle> list = new ArrayList<>(List.of(sub2, handle, sub1));
        Collections.sort(list);
        assertEquals(List.of(handle, sub1, sub2), list);
    }
}
