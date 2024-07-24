package me.datafox.dfxengine.dependencies.test;

import me.datafox.dfxengine.dependencies.AbstractDependency;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class TestDependency extends AbstractDependency {
    public TestDependency() {
        super(LoggerFactory.getLogger(TestDependency.class));
    }
}
