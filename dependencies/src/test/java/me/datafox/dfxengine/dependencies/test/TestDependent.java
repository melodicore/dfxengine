package me.datafox.dfxengine.dependencies.test;

import me.datafox.dfxengine.dependencies.AbstractDependent;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class TestDependent extends AbstractDependent {
    public TestDependent() {
        super(LoggerFactory.getLogger(TestDependent.class));
    }
}
