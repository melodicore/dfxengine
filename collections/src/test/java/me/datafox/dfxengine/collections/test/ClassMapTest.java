package me.datafox.dfxengine.collections.test;

import me.datafox.dfxengine.collections.FunctionClassMap;
import me.datafox.dfxengine.collections.ObjectClassMap;
import org.junit.jupiter.api.Test;

import java.io.FilterOutputStream;
import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
@Deprecated
public class ClassMapTest {
    @Test
    public void functionClassMap() {
        var functionClassMap = new FunctionClassMap<>(Object::getClass);

        functionClassMap.putAll(List.of("string", 420, 69d, 1337, System.out));

        assertEquals(List.of("string"), functionClassMap.get(String.class));
        assertEquals(List.of(420, 1337), functionClassMap.get(Integer.class));
        assertEquals(List.of(System.out), functionClassMap.get(FilterOutputStream.class));
        assertEquals(List.of(420, 69d, 1337), functionClassMap.get(Number.class));

        assertTrue(functionClassMap.contains(CharSequence.class));
        assertFalse(functionClassMap.contains(Long.class));

        assertTrue(functionClassMap.isSingleton(Double.class));
        assertFalse(functionClassMap.isSingleton(Serializable.class));
    }
    @Test
    public void objectClassMap() {
        var objectClassMap = new ObjectClassMap();

        objectClassMap.putAll(List.of(420, 69d, 1337));

        assertEquals(List.of(420, 69d, 1337),
                objectClassMap.getAndCast(Number.class));
    }
}
