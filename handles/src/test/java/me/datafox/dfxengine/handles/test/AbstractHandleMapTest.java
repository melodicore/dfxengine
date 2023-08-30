package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static me.datafox.dfxengine.handles.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public abstract class AbstractHandleMapTest extends AbstractTest {
    protected HandleMap<Object> testMap;

    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();

        testMap = getMap(testSpace);

        testMap.put(testHandle, TEST_STRING);

        testMap.put(otherHandle, TEST_INTEGER);
    }

    protected abstract HandleMap<Object> getMap(Space space);

    @Test
    public void putTest() {
        assertEquals(TEST_STRING, testMap.put(testHandle, TEST_BOOLEAN));

        assertEquals(Map.of(testHandle, TEST_BOOLEAN, otherHandle, TEST_INTEGER), testMap);
    }

    @Test
    public void putAllTest() {
        testMap.putAll(Map.of(testHandle, TEST_BOOLEAN, otherHandle, TEST_CHARACTER));

        assertEquals(Map.of(testHandle, TEST_BOOLEAN, otherHandle, TEST_CHARACTER), testMap);

        assertTrue(testMap.containsAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));
    }

    @Test
    public void removeTest() {
        assertEquals(TEST_STRING, testMap.remove(testHandle));

        assertEquals(Map.of(otherHandle, TEST_INTEGER), testMap);
    }

    @Test
    public void clearTest() {
        testMap.clear();

        assertEquals(Map.of(), testMap);
    }

    @Test
    public void putHandledTest() {
        Handled handled = () -> testHandle;

        assertEquals(TEST_STRING, testMap.putHandled(handled));

        assertEquals(handled, testMap.get(testHandle));

        assertThrows(IllegalArgumentException.class, () -> testMap.putHandled(TEST_STRING));

        assertEquals(handled, testMap.get(testHandle));
    }

    @Test
    public void containsByIdTest() {
        assertTrue(testMap.containsById(TEST_HANDLE));

        assertFalse(testMap.containsById(HANDLE_ID));
    }

    @Test
    public void containsAllTest() {
        assertTrue(testMap.containsAll(Set.of(testHandle, otherHandle)));

        assertFalse(testMap.containsAll(Set.of(testHandle, testTag)));
    }

    @Test
    public void containsAllByIdTest() {
        assertTrue(testMap.containsAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertFalse(testMap.containsAllById(Set.of(TEST_HANDLE, HANDLE_ID)));
    }

    @Test
    public void getByIdTest() {
        assertEquals(TEST_STRING, testMap.getById(TEST_HANDLE));

        assertNull(testMap.getById(HANDLE_ID));
    }

    @Test
    public void removeByIdTest() {
        assertEquals(TEST_STRING, testMap.removeById(TEST_HANDLE));

        assertEquals(Map.of(otherHandle, TEST_INTEGER), testMap);
    }

    @Test
    public void removeAllTest() {
        assertTrue(testMap.removeAll(Set.of(testHandle, testTag)));

        assertEquals(Map.of(otherHandle, TEST_INTEGER), testMap);

        assertTrue(testMap.removeAll(Set.of(testHandle, otherHandle)));

        assertEquals(Map.of(), testMap);

        assertFalse(testMap.removeAll(Set.of(testHandle, otherHandle)));
    }

    @Test
    public void removeAllByIdTest() {
        assertTrue(testMap.removeAllById(Set.of(TEST_HANDLE, HANDLE_ID)));

        assertEquals(Map.of(otherHandle, TEST_INTEGER), testMap);

        assertTrue(testMap.removeAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertEquals(Map.of(), testMap);

        assertFalse(testMap.removeAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));
    }
}
