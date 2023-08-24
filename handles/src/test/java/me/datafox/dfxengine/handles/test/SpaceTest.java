package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static me.datafox.dfxengine.handles.test.TestStrings.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class SpaceTest {
    private static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    private Space testSpace;

    private Handle testHandle;

    private Handle otherHandle;

    private Handle testTag;

    private Handle otherTag;

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        testSpace = handleManager.createSpace(TEST_SPACE);

        testHandle = testSpace.createHandle(TEST_HANDLE);
        otherHandle = testSpace.createHandle(OTHER_HANDLE);

        testTag = handleManager.createTag(TEST_TAG);
        otherTag = handleManager.createTag(OTHER_TAG);

        testHandle.addTag(testTag);
        testHandle.addTag(otherTag);
        otherHandle.addTag(testTag);
    }

    @Test
    public void getHandleManagerTest() {
        assertEquals(handleManager, testSpace.getHandleManager());
    }

    @Test
    public void getHandleTest() {
        assertEquals(handleManager.getSpaceHandle(TEST_SPACE), testSpace.getHandle());
    }

    @Test
    public void getIdTest() {
        assertEquals(TEST_SPACE, testSpace.getId());
    }

    @Test
    public void isHandleTest() {
        assertTrue(testSpace.isHandle(handleManager.getSpaceHandle(TEST_SPACE)));

        assertFalse(testSpace.isHandle(handleManager.createSpace(SPACE_ID).getHandle()));
    }

    @Test
    public void isIdTest() {
        assertTrue(testSpace.isId(TEST_SPACE));

        assertFalse(testSpace.isId(SPACE_ID));
    }

    @Test
    public void getHandleIdTest() {
        assertEquals(testHandle, testSpace.getHandle(TEST_HANDLE));
    }

    @Test
    public void getHandlesTest() {
        assertEquals(Set.of(testHandle, otherHandle), testSpace.getHandles());
    }

    @Test
    public void getHandlesByTagTest() {
        assertEquals(Set.of(testHandle, otherHandle), testSpace.getHandlesByTag(testTag));

        assertEquals(Set.of(testHandle), testSpace.getHandlesByTag(otherTag));
    }

    @Test
    public void getHandlesByTagIdTest() {
        assertEquals(Set.of(testHandle, otherHandle), testSpace.getHandlesByTagId(TEST_TAG));

        assertEquals(Set.of(testHandle), testSpace.getHandlesByTagId(OTHER_TAG));
    }

    @Test
    public void getHandlesByTagsTest() {
        assertEquals(Set.of(testHandle), testSpace.getHandlesByTags(Set.of(testTag, otherTag)));
    }

    @Test
    public void getHandlesByTagIdsTest() {
        assertEquals(Set.of(testHandle), testSpace.getHandlesByTagIds(Set.of(TEST_TAG, OTHER_TAG)));
    }

    @Test
    public void createHandleTest() {
        assertEquals(HANDLE_ID, testSpace.createHandle(HANDLE_ID).getId());

        assertThrows(IllegalArgumentException.class, () -> testSpace.createHandle(HANDLE_ID));
    }

    @Test
    public void getOrCreateHandleTest() {
        assertEquals(HANDLE_ID, testSpace.getOrCreateHandle(HANDLE_ID).getId());

        assertEquals(HANDLE_ID, testSpace.getOrCreateHandle(HANDLE_ID).getId());
    }
}
