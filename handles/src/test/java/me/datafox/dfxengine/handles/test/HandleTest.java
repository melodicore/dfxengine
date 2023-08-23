package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.HandleManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

        var space1 = handleManager.createSpace("testSpace1");

        space1.createHandle("testHandle1").addTagsById(Set.of("allTag", "oneTag"));
        space1.createHandle("testHandle2").addTagsById(Set.of("allTag", "twoTag"));
        space1.createHandle("testHandle3").addTagsById(Set.of("allTag", "threeTag"));

        var space2 = handleManager.createSpace("testSpace2");

        space2.createHandle("testHandle1");
        space2.createHandle("testHandle2");
        space2.createHandle("testHandle3");
    }

    @Test
    public void getSpace_pass() {
        var testSpace1 = handleManager.getSpaceById("testSpace1");

        assertEquals("testSpace1", testSpace1.getId());
    }

    @Test
    public void createSpace_pass() {
        var createdSpace = handleManager.createSpace("createdSpace");

        assertEquals("createdSpace", createdSpace.getId());
    }

    @Test
    public void removeSpace_pass() {
        handleManager.removeSpaceById("testSpace1");

        assertNull(handleManager.getSpaceById("testSpace1"));
    }

    @Test
    public void getHandle_pass() {
        var testHandle1 = handleManager.getSpaceById("testSpace1").getHandle("testHandle1");

        assertEquals("testHandle1", testHandle1.getId());

        assertEquals(testHandle1, handleManager.getSpaceById("testSpace1").getHandles().iterator().next());
    }

    @Test
    public void createHandle_pass() {
        var createdHandle = handleManager.getSpaceById("testSpace1").createHandle("createdHandle");

        assertEquals("createdHandle", createdHandle.getId());

        assertEquals("testSpace1", createdHandle.getSpace().getId());
    }

    @Test
    public void removeHandle_pass() {
        var testSpace1 = handleManager.getSpaceById("testSpace1");

        testSpace1.removeHandleById("testHandle1");

        assertNull(testSpace1.getHandle("testHandle1"));
    }

    @Test
    public void sameIdNotEquals_pass() {
        var testHandle11 = handleManager.getSpaceById("testSpace1").getHandle("testHandle1");

        var testHandle12 = handleManager.getSpaceById("testSpace2").getHandle("testHandle1");

        assertEquals(testHandle11.getId(), testHandle12.getId());

        assertNotEquals(testHandle11, testHandle12);
    }

    @Test
    public void getByTag_pass() {
        var handles = handleManager.getSpaceById("testSpace1").getHandlesByTagId("allTag");

        assertEquals(3, handles.size());

        assertEquals("testHandle1", handleManager.getSpaceById("testSpace1").getHandles().iterator().next().getId());
    }

    @Test
    public void getByTags_pass() {
        var handles = handleManager.getSpaceById("testSpace1").getHandlesByTagIds(Set.of("allTag", "oneTag"));

        assertEquals(1, handles.size());

        assertEquals("testHandle1", handleManager.getSpaceById("testSpace1").getHandles().iterator().next().getId());
    }
}
