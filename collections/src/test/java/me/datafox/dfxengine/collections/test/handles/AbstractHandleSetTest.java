package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static me.datafox.dfxengine.collections.test.handles.HandleCollectionTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public abstract class AbstractHandleSetTest extends AbstractHandleCollectionTest {
    protected HandleSet testSet;

    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();

        testSet = getSet(testSpace);

        testSet.add(testHandle);

        testSet.add(otherHandle);
    }

    protected abstract HandleSet getSet(Space space);

    @Test
    public void addTest() {
        var createdHandle = testSpace.createHandle(HANDLE_ID);

        assertTrue(testSet.add(createdHandle));

        assertEquals(Set.of(testHandle, otherHandle, createdHandle), testSet);

        assertFalse(testSet.add(createdHandle));

        assertThrows(IllegalArgumentException.class, () -> testSet.add(testHandle.getSpace().getHandle()));
    }

    @Test
    public void removeTest() {
        assertTrue(testSet.remove(testHandle));

        assertEquals(Set.of(otherHandle), testSet);

        assertFalse(testSet.remove(testHandle));
    }

    @Test
    public void addAllTest() {
        var createdHandle = testSpace.createHandle(HANDLE_ID);

        assertTrue(testSet.addAll(Set.of(testHandle, createdHandle)));

        assertEquals(Set.of(testHandle, otherHandle, createdHandle), testSet);

        assertTrue(testSet.containsAllById(Set.of(TEST_HANDLE, OTHER_HANDLE, HANDLE_ID)));

        assertFalse(testSet.addAll(Set.of(testHandle, createdHandle)));
    }

    @Test
    public void removeAllTest() {
        assertTrue(testSet.removeAll(Set.of(testHandle, testTag)));

        assertEquals(Set.of(otherHandle), testSet);

        assertTrue(testSet.removeAll(Set.of(testHandle, otherHandle)));

        assertEquals(Set.of(), testSet);

        assertFalse(testSet.removeAll(Set.of(testHandle, otherHandle)));
    }

    @Test
    public void clearTest() {
        testSet.clear();

        assertEquals(Set.of(), testSet);
    }

    @Test
    public void containsByIdTest() {
        assertTrue(testSet.containsById(TEST_HANDLE));

        assertFalse(testSet.containsById(HANDLE_ID));
    }

    @Test
    public void containsAllByIdTest() {
        assertTrue(testSet.containsAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertFalse(testSet.containsAllById(Set.of(TEST_HANDLE, HANDLE_ID)));
    }

    @Test
    public void getTest() {
        assertEquals(testHandle, testSet.get(TEST_HANDLE));

        assertNull(testSet.get(HANDLE_ID));
    }

    @Test
    public void removeByIdTest() {
        assertTrue(testSet.removeById(TEST_HANDLE));

        assertEquals(Set.of(otherHandle), testSet);

        assertFalse(testSet.removeById(TEST_HANDLE));
    }

    @Test
    public void removeAllByIdTest() {
        assertTrue(testSet.removeAllById(Set.of(TEST_HANDLE, HANDLE_ID)));

        assertEquals(Set.of(otherHandle), testSet);

        assertTrue(testSet.removeAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));

        assertEquals(Set.of(), testSet);

        assertFalse(testSet.removeAllById(Set.of(TEST_HANDLE, OTHER_HANDLE)));
    }
}
