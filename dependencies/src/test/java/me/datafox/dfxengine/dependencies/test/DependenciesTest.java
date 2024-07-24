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

        root.addDependent(rootDependency);
        root.addDependent(rootCombined);
        rootCombined.addDependent(combinedDependency);
        rootCombined.addDependent(combinedCombined);
    }

    @Test
    public void invalidateTest() {
        root.invalidateDependents();

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
        assertThrows(IllegalArgumentException.class, () -> combinedCombined.addDependent(rootCombined));
    }

    @Test
    public void getDependenciesTest() {
        assertEquals(Set.of(rootDependency, rootCombined), root.getDependents());
    }

    @Test
    public void addDependencyTest() {
        assertTrue(combinedCombined.addDependent(rootDependency));

        root.invalidateDependents();

        assertEquals(2, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(combinedCombined.addDependent(rootDependency));
    }

    @Test
    public void addDependenciesTest() {
        assertTrue(root.addDependents(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependents();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependency.i());
        assertEquals(2, combinedCombined.i());

        assertTrue(root.addDependents(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependents();

        assertEquals(2, rootDependency.i());
        assertEquals(2, rootCombined.i());
        assertEquals(3, combinedDependency.i());
        assertEquals(4, combinedCombined.i());

        assertFalse(root.addDependents(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependents();

        assertEquals(3, rootDependency.i());
        assertEquals(3, rootCombined.i());
        assertEquals(5, combinedDependency.i());
        assertEquals(6, combinedCombined.i());
    }

    @Test
    public void removeDependencyTest() {
        assertTrue(rootCombined.removeDependent(combinedDependency));

        root.invalidateDependents();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependent(combinedDependency));
    }

    @Test
    public void removeDependenciesTest() {
        assertTrue(rootCombined.removeDependents(Set.of(rootDependency, combinedDependency)));

        root.invalidateDependents();

        assertEquals(1, rootDependency.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertTrue(rootCombined.removeDependents(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependents();

        assertEquals(2, rootDependency.i());
        assertEquals(2, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependents(Set.of(rootDependency, combinedCombined)));

        root.invalidateDependents();

        assertEquals(3, rootDependency.i());
        assertEquals(3, rootCombined.i());
        assertEquals(0, combinedDependency.i());
        assertEquals(1, combinedCombined.i());
    }

    @Test
    public void containsDependencyTest() {
        assertTrue(root.containsDependent(rootDependency));
        assertTrue(root.containsDependent(rootCombined));
        assertFalse(root.containsDependent(combinedDependency));
        assertFalse(root.containsDependent(combinedCombined));
    }

    @Test
    public void containsDependenciesTest() {
        assertTrue(root.containsDependents(Set.of(rootDependency, rootCombined)));
        assertFalse(root.containsDependents(Set.of(rootDependency, combinedDependency)));
    }

    @Test
    public void containsDependencyRecursive() {
        assertTrue(root.containsDependentRecursive(rootDependency));
        assertTrue(root.containsDependentRecursive(rootCombined));
        assertTrue(root.containsDependentRecursive(combinedDependency));
        assertTrue(root.containsDependentRecursive(combinedCombined));
    }

    @Test
    public void containsDependenciesRecursiveTest() {
        assertTrue(root.containsDependentsRecursive(Set.of(rootDependency, rootCombined)));
        assertTrue(root.containsDependentsRecursive(Set.of(rootDependency, combinedDependency)));
    }

    @Test
    public void dependencyStreamTest() {
        assertEquals(Set.of(rootDependency, rootCombined), root.dependentStream().collect(Collectors.toSet()));
    }

    @Test
    public void recursiveDependencyStreamTest() {
        assertEquals(Set.of(rootDependency, rootCombined, combinedDependency, combinedCombined), root.recursiveDependentStream().collect(Collectors.toSet()));
    }
}
