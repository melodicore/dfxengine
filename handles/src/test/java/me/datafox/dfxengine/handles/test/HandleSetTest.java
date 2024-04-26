package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerConfiguration;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.HashHandleSet;
import me.datafox.dfxengine.handles.TreeHandleSet;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleSetTest {
    private Space space;
    private Handle handle1, handle2, handle3;

    @BeforeEach
    public void beforeEach() {
        HandleManager hm = new HandleManagerImpl(LoggerFactory.getLogger(HandleManagerImpl.class),
                HandleManagerConfiguration.defaultConfiguration());
        space = hm.createSpace("space");
        handle1 = space.createHandle("handle1");
        handle2 = space.createHandle("handle2");
        handle3 = space.createHandle("handle3");
    }

    private HandleSet hashHandleSet() {
        return new HashHandleSet(space, LoggerFactory.getLogger(HashHandleSet.class));
    }

    private HandleSet treeHandleSet() {
        return new TreeHandleSet(space, LoggerFactory.getLogger(TreeHandleSet.class));
    }

    @Test
    public void getTest() {
        getTest(hashHandleSet());
        getTest(treeHandleSet());
    }

    private void getTest(HandleSet set) {
        set.add(handle1);
        assertSame(handle1, set.get("handle1"));
        assertNull(set.get("handle2"));
        assertThrows(NullPointerException.class, () -> set.get(null));
    }

    @Test
    public void addTest() {
        addTest(hashHandleSet());
        addTest(treeHandleSet());
    }

    private void addTest(HandleSet set) {
        assertTrue(set.add(handle1));
        Handle _handle2 = assertDoesNotThrow(() -> set.add("handle2"));
        assertSame(handle2, _handle2);
        assertFalse(set.add(handle2));
        assertThrows(NullPointerException.class, () -> set.add((String) null));
        assertThrows(NullPointerException.class, () -> set.add((Handle) null));
        assertThrows(IllegalArgumentException.class, () -> set.add(space.getHandle()));
        assertNotNull(set.add("handle4"));
        Handle sub = assertDoesNotThrow(() -> set.add("handle1:sub"));
        assertSame(handle1.getSubHandles().iterator().next(), sub);
        assertTrue(set.addAll(Set.of(handle2, handle3)));
        assertFalse(set.addAll(Set.of(handle2, handle3)));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(handle2);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> set.addAll(nullSet));
        assertThrows(NullPointerException.class, () -> set.addAll(null));
    }

    @Test
    public void unmodifiableTest() {
        unmodifiableTest(hashHandleSet());
        unmodifiableTest(treeHandleSet());
    }

    private void unmodifiableTest(HandleSet set) {
        HandleSet unmodifiable = set.unmodifiable();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("test"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add(handle1));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.remove(handle1));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.addAll(List.of(handle1, handle2)));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.retainAll(List.of(handle1, handle2)));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.removeAll(List.of(handle1, handle2)));
        assertThrows(UnsupportedOperationException.class, unmodifiable::clear);
        assertFalse(unmodifiable.contains(handle1));
        set.add(handle1);
        assertTrue(unmodifiable.contains(handle1));
    }

    @Test
    public void tagTest() {
        tagTest(hashHandleSet());
        tagTest(treeHandleSet());
    }

    private void tagTest(HandleSet set) {
        Handle tag1 = handle1.getTags().add("tag1");
        Handle tag2 = handle1.getTags().add("tag2");
        handle2.getTags().add(tag2);
        Handle tag3 = handle2.getTags().add("tag3");
        set.addAll(Set.of(handle1, handle2));
        assertEquals(Set.of(handle1), set.getByTag(tag1));
        assertEquals(Set.of(handle1, handle2), set.getByTag(tag2));
        assertEquals(Set.of(handle2), set.getByTag("tag3"));
        assertEquals(Set.of(), set.getByTag("tag4"));
        assertThrows(ClassCastException.class, () -> set.getByTag(316L));
        assertThrows(NullPointerException.class, () -> set.getByTag(null));
        assertThrows(IllegalArgumentException.class, () -> set.getByTag(handle1));
        assertEquals(Set.of(handle1), set.getByTags(Set.of(tag1, tag2)));
        assertEquals(Set.of(handle2), set.getByTags(Set.of("tag2", tag3)));
        assertEquals(Set.of(), set.getByTags(Set.of(tag1, "tag3")));
        assertEquals(Set.of(), set.getByTags(Set.of(tag1, "not a tag")));
        assertThrows(ClassCastException.class, () -> set.getByTags(Set.of(tag1, 42.0)));
        assertThrows(IllegalArgumentException.class, () -> set.getByTags(Set.of(tag1, handle1)));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(tag1);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> set.getByTags(nullSet));
        assertThrows(NullPointerException.class, () -> set.getByTags(null));
    }

    @Test
    public void containsTest() {
        containsTest(hashHandleSet());
        containsTest(treeHandleSet());
    }

    private void containsTest(HandleSet set) {
        set.add(handle1);
        set.add(handle2);
        assertTrue(set.contains(handle1));
        assertTrue(set.contains("handle1"));
        assertFalse(set.contains(handle3));
        assertFalse(set.contains("not a tag"));
        assertThrows(ClassCastException.class, () -> set.contains(true));
        assertThrows(NullPointerException.class, () -> set.contains(null));
        assertTrue(set.containsAll(Set.of(handle1, handle2)));
        assertTrue(set.containsAll(Set.of("handle1", "handle2")));
        assertTrue(set.containsAll(Set.of(handle1, "handle2")));
        assertFalse(set.containsAll(Set.of(handle1, handle3)));
        assertFalse(set.containsAll(Set.of("handle1", "handle3")));
        assertFalse(set.containsAll(Set.of(handle2, "not a handle")));
        assertThrows(ClassCastException.class, () -> set.containsAll(Set.of(handle3, false)));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(handle1);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> set.containsAll(nullSet));
        assertThrows(NullPointerException.class, () -> set.containsAll(null));
    }

    @Test
    public void removeTest() {
        removeTest(hashHandleSet());
        removeTest(treeHandleSet());
    }

    private void removeTest(HandleSet set) {
        set.add(handle1);
        assertTrue(set.remove(handle1));
        assertFalse(set.remove(handle1));
        set.add(handle1);
        assertTrue(set.remove("handle1"));
        assertFalse(set.remove("handle1"));
        assertThrows(ClassCastException.class, () -> set.remove((byte) 6));
        assertThrows(NullPointerException.class, () -> set.remove(null));
        set.addAll(Set.of(handle1, handle2));
        assertTrue(set.removeAll(Set.of(handle1, handle3)));
        assertFalse(set.removeAll(Set.of(handle1, handle3)));
        assertEquals(1, set.size());
        set.add(handle1);
        assertTrue(set.removeAll(Set.of("handle1", handle2)));
        assertFalse(set.removeAll(Set.of("handle1", handle2)));
        assertTrue(set.isEmpty());
        assertThrows(ClassCastException.class, () -> set.removeAll(Set.of(handle1, (Supplier<Integer>) this::hashCode)));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(handle1);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> set.removeAll(nullSet));
        assertThrows(NullPointerException.class, () -> set.removeAll(null));

    }

    @Test
    public void retainAllTest() {
        retainAllTest(hashHandleSet());
        retainAllTest(treeHandleSet());
    }

    private void retainAllTest(HandleSet set) {
        set.addAll(Set.of(handle1, handle2, handle3));
        assertTrue(set.retainAll(Set.of(handle1, "handle2")));
        assertFalse(set.retainAll(Set.of(handle1, "handle2")));
        assertTrue(set.containsAll(Set.of("handle1", handle2)));
        assertEquals(2, set.size());
        assertThrows(ClassCastException.class, () -> set.retainAll(Set.of(handle1, 'd')));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(handle1);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> set.retainAll(nullSet));
        assertThrows(NullPointerException.class, () -> set.retainAll(null));
    }
}
