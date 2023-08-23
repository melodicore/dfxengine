package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class HandleTest {
    private static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        Space space = handleManager.createSpace("testSpace");

        space.createHandle("testHandle1");
        space.createHandle("testHandle2");
        space.createHandle("testHandle3");
    }

    @Test
    public void getHandle_pass() {
        Handle testHandle1 = handleManager.getSpaceById("testSpace").getHandle("testHandle1");

        assertEquals("testHandle1", testHandle1.getId());

        assertEquals(testHandle1, handleManager.getSpaceById("testSpace").getHandles().iterator().next());
    }
}
