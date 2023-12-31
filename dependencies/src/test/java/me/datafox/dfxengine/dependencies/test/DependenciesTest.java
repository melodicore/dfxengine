package me.datafox.dfxengine.dependencies.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class DependenciesTest {
    private TestDependent root;

    private TestDependency rootDependency;

    private TestCombined rootCombined;

    private TestDependency combinedDependency;

    private TestCombined combinedCombined;

    @BeforeEach
    public void beforeEach() {
        root = new TestDependent();
        rootDependency = new TestDependency();
        rootCombined = new TestCombined();
        combinedDependency = new TestDependency();
        combinedCombined = new TestCombined();

        root.addDependency(rootDependency);
        root.addDependency(rootCombined);
        rootCombined.addDependency(combinedDependency);
        rootCombined.addDependency(combinedCombined);
    }

    @Test
    public void invalidateTest() {
        root.invalidateDependencies();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        rootCombined.invalidate();

        assertEquals(1, rootDependency.i());
        assertEquals(2, rootCombined.i());
        assertEquals(2, combinedDependency.i());
        assertEquals(2, combinedCombined.i());
    }

    @Test
    public void cyclicDependencyTest() {
        assertThrows(IllegalArgumentException.class, () -> combinedCombined.addDependency(rootCombined));
    }

    @Test
    public void getDependenciesTest() {
        assertEquals(Set.of(rootDependency, rootCombined), root.getDependencies());
    }

    @Test
    public void addDependencyTest() {
        assertTrue(combinedCombined.addDependency(rootDependency));

        root.invalidateDependencies();

        assertEquals(2, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(combinedCombined.addDependency(rootDependency));
    }

    @Test
    public void addDependenciesTest() {
        assertTrue(root.addDependencies(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependencies();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependency.i());
        assertEquals(2, combinedCombined.i());

        assertTrue(root.addDependencies(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependencies();

        assertEquals(2, rootDependency.i());
        assertEquals(2, rootCombined.i());
        assertEquals(3, combinedDependency.i());
        assertEquals(4, combinedCombined.i());

        assertFalse(root.addDependencies(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependencies();

        assertEquals(3, rootDependency.i());
        assertEquals(3, rootCombined.i());
        assertEquals(5, combinedDependency.i());
        assertEquals(6, combinedCombined.i());
    }

    @Test
    public void removeDependencyTest() {
        assertTrue(rootCombined.removeDependency(combinedDependency));

        root.invalidateDependencies();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependency(combinedDependency));
    }

    @Test
    public void removeDependenciesTest() {
        assertTrue(rootCombined.removeDependencies(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependencies();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertTrue(rootCombined.removeDependencies(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependencies();

        assertEquals(2, rootDependency.i());
        assertEquals(2, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependencies(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependencies();

        assertEquals(3, rootDependency.i());
        assertEquals(3, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());
    }

    @Test
    public void containsDependencyTest() {
        assertTrue(root.containsDependency(rootDependency));
        assertTrue(root.containsDependency(rootCombined));
        assertFalse(root.containsDependency(combinedDependency));
        assertFalse(root.containsDependency(combinedCombined));
    }

    @Test
    public void containsDependenciesTest() {
        assertTrue(root.containsDependencies(Set.of(rootDependency, rootCombined)));
        assertFalse(root.containsDependencies(Set.of(rootDependency, combinedDependency)));
    }

    @Test
    public void containsDependencyRecursive() {
        assertTrue(root.containsDependencyRecursive(rootDependency));
        assertTrue(root.containsDependencyRecursive(rootCombined));
        assertTrue(root.containsDependencyRecursive(combinedDependency));
        assertTrue(root.containsDependencyRecursive(combinedCombined));
    }

    @Test
    public void containsDependenciesRecursiveTest() {
        assertTrue(root.containsDependenciesRecursive(Set.of(rootDependency, rootCombined)));
        assertTrue(root.containsDependenciesRecursive(Set.of(rootDependency, combinedDependency)));
    }

    @Test
    public void dependencyStreamTest() {
        assertEquals(Set.of(rootDependency, rootCombined), root.dependencyStream().collect(Collectors.toSet()));
    }

    @Test
    public void recursiveDependencyStreamTest() {
        assertEquals(Set.of(rootDependency, rootCombined, combinedDependency, combinedCombined), root.recursiveDependencyStream().collect(Collectors.toSet()));
    }
}
