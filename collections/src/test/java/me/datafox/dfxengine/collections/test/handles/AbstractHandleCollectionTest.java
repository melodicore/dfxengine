package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import static me.datafox.dfxengine.collections.test.handles.HandleCollectionTestConstants.*;

/**
 * @author datafox
 */
public abstract class AbstractHandleCollectionTest {
    protected static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    protected Space testSpace;

    protected Handle testHandle;

    protected Handle otherHandle;

    protected Handle testTag;

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        testSpace = handleManager.createSpace(TEST_SPACE);

        testHandle = testSpace.createHandle(TEST_HANDLE);
        otherHandle = testSpace.createHandle(OTHER_HANDLE);

        testTag = handleManager.createTag(TEST_TAG);

        testHandle.addTag(testTag);
        otherHandle.addTag(testTag);
    }
}
