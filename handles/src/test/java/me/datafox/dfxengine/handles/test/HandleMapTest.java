package me.datafox.dfxengine.handles.test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.handles.HandleManagerConfiguration;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleMapTest {
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

    private <T> HandleMap<T> hashHandleMap() {
        return new HashHandleMap<>(space, LoggerFactory.getLogger(HashHandleMap.class));
    }

    private <T> HandleMap<T> treeHandleMap() {
        return new TreeHandleMap<>(space, LoggerFactory.getLogger(TreeHandleMap.class));
    }

    @Test
    public void containsKeyTest() {
        containsKeyTest(hashHandleMap());
        containsKeyTest(treeHandleMap());
    }

    private void containsKeyTest(HandleMap<String> map) {
        map.put(handle1, "test1");
        map.put(handle2, "test1");
        assertTrue(map.containsKey(handle1));
        assertTrue(map.containsKey("handle2"));
        assertFalse(map.containsKey(handle3));
        assertFalse(map.containsKey("non-existent"));
        assertThrows(ClassCastException.class, () -> map.containsKey('x'));
        assertThrows(NullPointerException.class, () -> map.containsKey(null));
        assertTrue(map.containsKeys(Set.of(handle1, handle2)));
        assertTrue(map.containsKeys(Set.of(handle1, "handle2")));
        assertTrue(map.containsKeys(Set.of("handle1", "handle2")));
        assertFalse(map.containsKeys(Set.of(handle1, handle3)));
        assertFalse(map.containsKeys(Set.of("handle1", handle2, "was not here")));
        assertThrows(ClassCastException.class, () -> map.containsKeys(Set.of(handle1, 'y')));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(handle2);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> map.containsKeys(nullSet));
        assertThrows(NullPointerException.class, () -> map.containsKeys(null));
    }

    @Test
    public void putHandledTest() {
        putHandledTest(hashHandleMap());
        putHandledTest(treeHandleMap());
    }

    private void putHandledTest(HandleMap<TestClass> map) {
        TestHandled th1 = new TestHandled(handle1);
        TestHandled th2 = new TestHandled(handle2);
        assertNull(map.putHandled(th1));
        assertNull(map.putHandled(th2));
        assertSame(th1, map.putHandled(new TestHandled(handle1)));
        assertNotSame(th1, map.get(handle1));
        TestClass tc = new TestClass();
        assertThrows(ClassCastException.class, () -> map.putHandled(tc));
        assertSame(th2, map.put(handle2, tc));
        assertSame(tc, map.get(handle2));
        assertThrows(NullPointerException.class, () -> map.putHandled(null));
        assertThrows(IllegalArgumentException.class, () -> map.putHandled(new TestHandled(handle1.getSpace().getHandle())));
    }

    @Test
    public void unmodifiableTest() {
        unmodifiableTest(hashHandleMap());
        unmodifiableTest(treeHandleMap());
    }

    private void unmodifiableTest(HandleMap<String> map) {
        map.put(handle1, "test1");
        HandleMap<String> unmodifiable = map.unmodifiable();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.putHandled("handled"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.put(handle2, "test2"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.remove(handle1));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.putAll(Map.of(handle2, "test2", handle3, "test3")));
        assertThrows(UnsupportedOperationException.class, unmodifiable::clear);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.putIfAbsent(handle1, "test2"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.replaceAll((handle, s) -> "boo"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.replace(handle1, "handle1", "handle2"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.replace(handle1, "handle2"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.computeIfAbsent(handle1, Handle::getId));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.computeIfPresent(handle1, (handle, s) -> "boo"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.compute(handle1, (handle, s) -> "boo"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.merge(handle1, "handle2", (handle, s) -> "boo"));
        assertEquals("test1", unmodifiable.get(handle1));
        assertNull(unmodifiable.get("handle2"));
        map.put(handle2, "test2");
        assertEquals("test2", unmodifiable.get(handle2));
    }

    @Test
    public void tagTest() {
        tagTest(hashHandleMap());
        tagTest(treeHandleMap());
    }

    private void tagTest(HandleMap<String> map) {
        Handle tag1 = handle1.getTags().add("tag1");
        Handle tag2 = handle1.getTags().add("tag2");
        handle2.getTags().add(tag2);
        Handle tag3 = handle2.getTags().add("tag3");
        map.putAll(Map.of(handle1, "test1", handle2, "test2"));
        assertEquals(Set.of("test1"), map.getByTag(tag1));
        assertEquals(Set.of("test1", "test2"), map.getByTag(tag2));
        assertEquals(Set.of("test2"), map.getByTag("tag3"));
        assertEquals(Set.of(), map.getByTag("tag4"));
        assertThrows(ClassCastException.class, () -> map.getByTag(316L));
        assertThrows(NullPointerException.class, () -> map.getByTag(null));
        assertThrows(IllegalArgumentException.class, () -> map.getByTag(handle1));
        assertEquals(Set.of("test1"), map.getByTags(Set.of(tag1, tag2)));
        assertEquals(Set.of("test2"), map.getByTags(Set.of("tag2", tag3)));
        assertEquals(Set.of(), map.getByTags(Set.of(tag1, "tag3")));
        assertEquals(Set.of(), map.getByTags(Set.of(tag1, "not a tag")));
        assertThrows(ClassCastException.class, () -> map.getByTags(Set.of(tag1, 42.0)));
        assertThrows(IllegalArgumentException.class, () -> map.getByTags(Set.of(tag1, handle1)));
        Set<Handle> nullSet = new HashSet<>();
        nullSet.add(tag1);
        nullSet.add(null);
        assertThrows(NullPointerException.class, () -> map.getByTags(nullSet));
        assertThrows(NullPointerException.class, () -> map.getByTags(null));
    }

    @Test
    public void getTest() {
        getTest(hashHandleMap());
        getTest(treeHandleMap());
    }

    private void getTest(HandleMap<String> map) {
        map.put(handle1, "test");
        assertEquals("test", map.get(handle1));
        assertEquals("test", map.get("handle1"));
        assertNull(map.get(handle2));
        assertNull(map.get("handle2"));
        assertThrows(NullPointerException.class, () -> map.get(null));
        assertThrows(ClassCastException.class, () -> map.get(52));
    }

    @Test
    public void putTest() {
        putTest(hashHandleMap());
        putTest(treeHandleMap());
    }

    private void putTest(HandleMap<String> map) {
        assertNull(map.put(handle1, "test1"));
        assertNull(map.put(handle2, "test2"));
        assertEquals("test1", map.put(handle1, "test3"));
        assertThrows(NullPointerException.class, () -> map.put(handle1, null));
        assertThrows(NullPointerException.class, () -> map.put(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> map.put(handle1.getSpace().getHandle(), "lol"));
        assertEquals("test3", map.putIfAbsent(handle1, "test4"));
        assertNull(map.putIfAbsent(handle3, "test4"));
        assertThrows(NullPointerException.class, () -> map.putIfAbsent(handle1, null));
        assertThrows(NullPointerException.class, () -> map.putIfAbsent(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> map.putIfAbsent(handle1.getSpace().getHandle(), "lol"));
        map.clear();
        assertEquals(Map.of(), map);
        assertDoesNotThrow(() -> map.putAll(Map.of(handle1, "test1", handle2, "test2")));
        assertEquals(Map.of(handle1, "test1", handle2, "test2"), map);
        assertDoesNotThrow(() -> map.putAll(Map.of(handle2, "test22", handle3, "test3")));
        assertEquals(Map.of(handle1, "test1", handle2, "test22", handle3, "test3"), map);
        Map<Handle,String> nullKeyMap = new HashMap<>();
        nullKeyMap.put(handle1, "test5");
        nullKeyMap.put(null, "test2");
        Map<Handle,String> nullValueMap = new HashMap<>();
        nullValueMap.put(handle1, "test5");
        nullValueMap.put(handle2, null);
        assertThrows(NullPointerException.class, () -> map.putAll(nullKeyMap));
        assertThrows(NullPointerException.class, () -> map.putAll(nullValueMap));
        assertThrows(NullPointerException.class, () -> map.putAll(null));
        assertThrows(IllegalArgumentException.class, () -> map.putAll(Map.of(handle1, "test5", handle1.getSpace().getHandle(), "nope")));
        assertEquals("test1", map.get(handle1));
    }

    @Test
    public void removeTest() {
        removeTest(hashHandleMap());
        removeTest(treeHandleMap());
    }

    private void removeTest(HandleMap<String> map) {
        map.put(handle1, "test1");
        map.put(handle2, "test2");
        assertEquals("test1", map.remove(handle1));
        assertNull(map.remove(handle1));
        assertEquals(Map.of(handle2, "test2"), map);
        map.put(handle1, "test1");
        assertEquals("test1", map.remove("handle1"));
        assertNull(map.remove("handle1"));
        assertEquals(Map.of(handle2, "test2"), map);
        assertThrows(ClassCastException.class, () -> map.remove(666));
        assertThrows(NullPointerException.class, () -> map.remove(null));
        map.put(handle1, "test1");
        assertFalse(map.remove(handle1, "something"));
        assertEquals(Map.of(handle1, "test1", handle2, "test2"), map);
        assertTrue(map.remove(handle1, "test1"));
        assertEquals(Map.of(handle2, "test2"), map);
        map.put(handle1, "test1");
        assertFalse(map.remove("handle1", "something"));
        assertEquals(Map.of(handle1, "test1", handle2, "test2"), map);
        assertTrue(map.remove("handle1", "test1"));
        assertEquals(Map.of(handle2, "test2"), map);
        map.put(handle1, "test1");
        assertThrows(ClassCastException.class, () -> map.remove(6.66f, "lol"));
        assertThrows(NullPointerException.class, () -> map.remove(null, "test1"));
        assertThrows(NullPointerException.class, () -> map.remove(handle1, null));
        assertEquals(Map.of(handle1, "test1", handle2, "test2"), map);
    }

    private static class TestClass {

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class TestHandled extends TestClass implements Handled {
        private final Handle handle;
    }
}
