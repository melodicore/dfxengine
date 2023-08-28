package me.datafox.dfxengine.handles.test;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class SpaceTest extends AbstractTest {
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

        assertNull(testSpace.getHandle(HANDLE_ID));
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

        assertTrue(testSpace.containsHandleById(HANDLE_ID));

        assertThrows(IllegalArgumentException.class, () -> testSpace.createHandle(HANDLE_ID));
    }

    @Test
    public void getOrCreateHandleTest() {
        assertEquals(HANDLE_ID, testSpace.getOrCreateHandle(HANDLE_ID).getId());

        assertTrue(testSpace.containsHandleById(HANDLE_ID));

        assertEquals(HANDLE_ID, testSpace.getOrCreateHandle(HANDLE_ID).getId());
    }

    @Test
    public void containsHandleTest() {
        assertTrue(testSpace.containsHandle(testHandle));

        assertFalse(testSpace.containsHandle(testTag));
    }

    @Test
    public void containsHandleByIdTest() {
        assertTrue(testSpace.containsHandleById(TEST_HANDLE));

        assertFalse(testSpace.containsHandleById(HANDLE_ID));
    }

    @Test
    public void containsHandlesTest() {
        assertTrue(testSpace.containsHandles(Set.of(testHandle, otherHandle)));

        assertFalse(testSpace.containsHandles(Set.of(testHandle, testTag)));
    }

    @Test
    public void containsHandlesByIdTest() {
        assertTrue(testSpace.containsHandlesById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertFalse(testSpace.containsHandlesById(Set.of(TEST_HANDLE, TEST_TAG)));
    }

    @Test
    public void removeHandleTest() {
        assertTrue(testSpace.removeHandle(testHandle));

        assertEquals(Set.of(otherHandle), testSpace.getHandles());

        assertFalse(testSpace.removeHandle(testHandle));
    }

    @Test
    public void removeHandleByIdTest() {
        assertTrue(testSpace.removeHandleById(TEST_HANDLE));

        assertEquals(Set.of(otherHandle), testSpace.getHandles());

        assertFalse(testSpace.removeHandleById(TEST_HANDLE));
    }

    @Test
    public void removeHandlesTest() {
        assertTrue(testSpace.removeHandles(Set.of(testHandle, testTag)));

        assertEquals(Set.of(otherHandle), testSpace.getHandles());

        assertTrue(testSpace.removeHandles(Set.of(testHandle, otherHandle)));

        assertEquals(Set.of(), testSpace.getHandles());

        assertFalse(testSpace.removeHandles(Set.of(otherHandle, testTag)));
    }

    @Test
    public void removeHandlesByIdTest() {
        assertTrue(testSpace.removeHandlesById(Set.of(TEST_HANDLE, HANDLE_ID)));

        assertEquals(Set.of(otherHandle), testSpace.getHandles());

        assertTrue(testSpace.removeHandlesById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertEquals(Set.of(), testSpace.getHandles());

        assertFalse(testSpace.removeHandlesById(Set.of(OTHER_HANDLE, HANDLE_ID)));
    }

    @Test
    public void handleStreamTest() {
        assertEquals(Set.of(testHandle, otherHandle), testSpace.handleStream().collect(Collectors.toSet()));
    }

    @Test
    public void clearTest() {
        testSpace.clear();

        assertEquals(Set.of(), testSpace.getHandles());
    }
}
