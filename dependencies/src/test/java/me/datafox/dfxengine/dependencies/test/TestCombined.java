package me.datafox.dfxengine.dependencies.test;

import me.datafox.dfxengine.dependencies.DependencyDependent;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class TestCombined extends DependencyDependent {
    private int i = 0;

    public TestCombined() {
        super(LoggerFactory.getLogger(TestCombined.class));
    }

    @Override
    public void invalidate() {
        i++;
        super.invalidate();
    }

    public int i() {
        return i;
    }
}
