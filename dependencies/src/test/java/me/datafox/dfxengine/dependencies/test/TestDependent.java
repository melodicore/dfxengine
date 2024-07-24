package me.datafox.dfxengine.dependencies.test;

import me.datafox.dfxengine.dependencies.AbstractDependency;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class TestDependent extends AbstractDependency {
    public TestDependent() {
        super(LoggerFactory.getLogger(TestDependent.class));
    }
}
