package me.datafox.dfxengine.dependencies.test;

import me.datafox.dfxengine.dependencies.Dependency;

/**
 * @author datafox
 */
public class TestDependency implements Dependency {
    private int i = 0;

    @Override
    public void invalidate() {
        i++;
    }

    public int i() {
        return i;
    }
}
