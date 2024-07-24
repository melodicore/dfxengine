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
    private TestDependency root;

    private TestDependent rootDependent;

    private TestCombined rootCombined;

    private TestDependent combinedDependent;

    private TestCombined combinedCombined;

    @BeforeEach
    public void beforeEach() {
        root = new TestDependency();
        rootDependent = new TestDependent();
        rootCombined = new TestCombined();
        combinedDependent = new TestDependent();
        combinedCombined = new TestCombined();

        root.addDependent(rootDependent);
        root.addDependent(rootCombined);
        rootCombined.addDependent(combinedDependent);
        rootCombined.addDependent(combinedCombined);
    }

    @Test
    public void invalidateTest() {
        root.invalidateDependents();

        assertEquals(1, rootDependent.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependent.i());
        assertEquals(1, combinedCombined.i());

        rootCombined.invalidate();

        assertEquals(1, rootDependent.i());
        assertEquals(2, rootCombined.i());
        assertEquals(2, combinedDependent.i());
        assertEquals(2, combinedCombined.i());
    }

    @Test
    public void cyclicDependencyTest() {
        assertThrows(IllegalArgumentException.class, () -> combinedCombined.addDependent(rootCombined));
    }

    @Test
    public void getDependentsTest() {
        assertEquals(Set.of(rootDependent, rootCombined), root.getDependents());
    }

    @Test
    public void addDependentTest() {
        assertTrue(combinedCombined.addDependent(rootDependent));

        root.invalidateDependents();

        assertEquals(2, rootDependent.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependent.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(combinedCombined.addDependent(rootDependent));
    }

    @Test
    public void addDependentsTest() {
        assertTrue(root.addDependents(Set.of(rootDependent, combinedCombined)));

        root.invalidateDependents();

        assertEquals(1, rootDependent.i());
        assertEquals(1, rootCombined.i());
        assertEquals(1, combinedDependent.i());
        assertEquals(2, combinedCombined.i());

        assertTrue(root.addDependents(Set.of(rootDependent, combinedDependent)));

        root.invalidateDependents();

        assertEquals(2, rootDependent.i());
        assertEquals(2, rootCombined.i());
        assertEquals(3, combinedDependent.i());
        assertEquals(4, combinedCombined.i());

        assertFalse(root.addDependents(Set.of(rootDependent, combinedDependent)));

        root.invalidateDependents();

        assertEquals(3, rootDependent.i());
        assertEquals(3, rootCombined.i());
        assertEquals(5, combinedDependent.i());
        assertEquals(6, combinedCombined.i());
    }

    @Test
    public void removeDependentTest() {
        assertTrue(rootCombined.removeDependent(combinedDependent));

        root.invalidateDependents();

        assertEquals(1, rootDependent.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependent.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependent(combinedDependent));
    }

    @Test
    public void removeDependentsTest() {
        assertTrue(rootCombined.removeDependents(Set.of(rootDependent, combinedDependent)));

        root.invalidateDependents();

        assertEquals(1, rootDependent.i());
        assertEquals(1, rootCombined.i());
        assertEquals(0, combinedDependent.i());
        assertEquals(1, combinedCombined.i());

        assertTrue(rootCombined.removeDependents(Set.of(rootDependent, combinedCombined)));

        root.invalidateDependents();

        assertEquals(2, rootDependent.i());
        assertEquals(2, rootCombined.i());
        assertEquals(0, combinedDependent.i());
        assertEquals(1, combinedCombined.i());

        assertFalse(rootCombined.removeDependents(Set.of(rootDependent, combinedCombined)));

        root.invalidateDependents();

        assertEquals(3, rootDependent.i());
        assertEquals(3, rootCombined.i());
        assertEquals(0, combinedDependent.i());
        assertEquals(1, combinedCombined.i());
    }

    @Test
    public void containsDependentTest() {
        assertTrue(root.containsDependent(rootDependent));
        assertTrue(root.containsDependent(rootCombined));
        assertFalse(root.containsDependent(combinedDependent));
        assertFalse(root.containsDependent(combinedCombined));
    }

    @Test
    public void containsDependentsTest() {
        assertTrue(root.containsDependents(Set.of(rootDependent, rootCombined)));
        assertFalse(root.containsDependents(Set.of(rootDependent, combinedDependent)));
    }

    @Test
    public void containsDependentRecursive() {
        assertTrue(root.containsDependentRecursive(rootDependent));
        assertTrue(root.containsDependentRecursive(rootCombined));
        assertTrue(root.containsDependentRecursive(combinedDependent));
        assertTrue(root.containsDependentRecursive(combinedCombined));
    }

    @Test
    public void containsDependentsRecursiveTest() {
        assertTrue(root.containsDependentsRecursive(Set.of(rootDependent, rootCombined)));
        assertTrue(root.containsDependentsRecursive(Set.of(rootDependent, combinedDependent)));
    }

    @Test
    public void dependentStreamTest() {
        assertEquals(Set.of(rootDependent, rootCombined), root.dependentStream().collect(Collectors.toSet()));
    }

    @Test
    public void recursiveDependentStreamTest() {
        assertEquals(Set.of(rootDependent, rootCombined, combinedDependent, combinedCombined), root.recursiveDependentStream().collect(Collectors.toSet()));
    }
}
