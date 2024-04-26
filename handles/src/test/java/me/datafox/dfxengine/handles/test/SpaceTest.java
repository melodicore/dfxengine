package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerConfiguration;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author datafox
 */
public class SpaceTest {
    private Space space;

    @BeforeEach
    public void beforeEach() {
        HandleManager hm = new HandleManagerImpl(LoggerFactory.getLogger(HandleManagerImpl.class), HandleManagerConfiguration.defaultConfiguration());
        space = hm.createSpace("space");
    }

    @Test
    public void getHandlesTest() {
        Handle handle1 = space.createHandle("handle1");
        Handle handle2 = space.createHandle("handle2");
        Handle sub1 = handle1.createSubHandle("sub1");
        Handle sub2 = handle1.createSubHandle("sub2");
        Handle sub3 = handle2.createSubHandle("sub3");
        HandleSet set = space.getHandles();
        assertEquals(Set.of(handle1, handle2), set);
        Handle handle3 = space.createHandle("handle3");
        assertEquals(Set.of(handle1, handle2, handle3), set);
        assertEquals(Set.of(handle1, sub1, sub2, handle2, sub3, handle3), space.getAllHandles());
    }

    @Test
    public void handleTest() {
        Handle handle1 = assertDoesNotThrow(() -> space.createHandle("handle1"));
        Handle handle2 = assertDoesNotThrow(() -> space.getOrCreateHandle("handle2"));
        assertSame(handle1, space.getOrCreateHandle("handle1"));
        assertThrows(NullPointerException.class, () -> space.createHandle(null));
        assertThrows(IllegalArgumentException.class, () -> space.createHandle("handle2"));
        assertEquals(Set.of(handle1, handle2), space.getHandles());
    }

    @Test
    public void groupTest() {
        Group group1 = assertDoesNotThrow(() -> space.createGroup("group1"));
        Group group2 = assertDoesNotThrow(() -> space.getOrCreateGroup("group2"));
        assertSame(group1, space.getOrCreateGroup("group1"));
        assertThrows(NullPointerException.class, () -> space.createGroup(null));
        assertThrows(IllegalArgumentException.class, () -> space.createGroup("group2"));
        assertEquals(Map.of(group1.getHandle(), group1, group2.getHandle(), group2), space.getGroups());
    }
}
