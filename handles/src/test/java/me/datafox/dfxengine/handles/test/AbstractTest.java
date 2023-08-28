package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import static me.datafox.dfxengine.handles.test.TestConstants.*;

/**
 * @author datafox
 */
public abstract class AbstractTest {
    protected static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    protected Space testSpace;

    protected Handle otherHandle;

    protected Space otherSpace;

    protected Handle testHandle;

    protected Handle testTag;

    protected Handle otherTag;

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        testSpace = handleManager.createSpace(TEST_SPACE);
        otherSpace = handleManager.createSpace(OTHER_SPACE);

        testHandle = testSpace.createHandle(TEST_HANDLE);
        otherHandle = testSpace.createHandle(OTHER_HANDLE);

        testTag = handleManager.createTag(TEST_TAG);
        otherTag = handleManager.createTag(OTHER_TAG);

        testHandle.addTag(testTag);
        testHandle.addTag(otherTag);

        otherHandle.addTag(testTag);
    }
}
