package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.HandleConstants;
import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.test.TestStrings.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleManagerTest {
    private static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    private Space testSpace;

    private Space otherSpace;

    private Handle testHandle;

    private Handle testTag;

    private Handle otherTag;

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        testSpace = handleManager.createSpace(TEST_SPACE);
        otherSpace = handleManager.createSpace(OTHER_SPACE);
        testHandle = testSpace.createHandle(TEST_HANDLE);

        testTag = handleManager.createTag(TEST_TAG);
        otherTag = handleManager.createTag(OTHER_TAG);

        testHandle.addTag(testTag);
        testHandle.addTag(otherTag);
    }

    @Test
    public void getSpaceHandleTest() {
        assertEquals(testSpace.getHandle(), handleManager.getSpaceHandle(TEST_SPACE));
    }

    @Test
    public void getSpaceHandlesTest() {
        assertEquals(Set.of(testSpace.getHandle(), otherSpace.getHandle(), testSpace.getHandle().getSpace().getHandle(), testTag.getSpace().getHandle()), Set.copyOf(handleManager.getSpaceHandles()));
    }

    @Test
    public void getSpaceTest() {
        assertEquals(testSpace, handleManager.getSpace(testSpace.getHandle()));
    }

    @Test
    public void getSpaceByIdTest() {
        assertEquals(testSpace, handleManager.getSpaceById(TEST_SPACE));
    }

    @Test
    public void getSpacesTest() {
        assertEquals(Set.of(testSpace, otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));
    }

    @Test
    public void createSpaceTest() {
        assertEquals(SPACE_ID, handleManager.createSpace(SPACE_ID).getId());

        assertTrue(handleManager.containsSpaceById(SPACE_ID));

        assertThrows(IllegalArgumentException.class, () -> handleManager.createSpace(SPACE_ID));
    }

    @Test
    public void getOrCreateSpaceTest() {
        assertEquals(SPACE_ID, handleManager.getOrCreateSpace(SPACE_ID).getId());

        assertTrue(handleManager.containsSpaceById(SPACE_ID));

        assertEquals(SPACE_ID, handleManager.getOrCreateSpace(SPACE_ID).getId());
    }

    @Test
    public void containsSpaceTest() {
        assertTrue(handleManager.containsSpace(testSpace));
    }

    @Test
    public void containsSpaceByHandleTest() {
        assertTrue(handleManager.containsSpaceByHandle(testSpace.getHandle()));

        assertFalse(handleManager.containsSpaceByHandle(testHandle));
    }

    @Test
    public void containsSpaceByIdTest() {
        assertTrue(handleManager.containsSpaceById(TEST_SPACE));

        assertFalse(handleManager.containsSpaceById(SPACE_ID));
    }

    @Test
    public void containsSpacesTest() {
        assertTrue(handleManager.containsSpaces(Set.of(testSpace, testSpace.getHandle().getSpace())));
    }

    @Test
    public void containsSpacesByHandleTest() {
        assertTrue(handleManager.containsSpacesByHandle(Set.of(testSpace.getHandle(), testSpace.getHandle().getSpace().getHandle())));

        assertFalse(handleManager.containsSpacesByHandle(Set.of(testSpace.getHandle(), testHandle)));
    }

    @Test
    public void containsSpacesByIdTest() {
        assertTrue(handleManager.containsSpacesById(Set.of(TEST_SPACE, HandleConstants.SPACES_ID)));

        assertFalse(handleManager.containsSpacesById(Set.of(TEST_SPACE, SPACE_ID)));
    }

    @Test
    public void removeSpaceTest() {
        assertTrue(handleManager.removeSpace(testSpace));

        assertEquals(Set.of(otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpace(testSpace));

        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpace(testSpace.getHandle().getSpace()));
    }

    @Test
    public void removeSpaceByHandleTest() {
        assertTrue(handleManager.removeSpaceByHandle(testSpace.getHandle()));

        assertEquals(Set.of(otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpaceByHandle(testSpace.getHandle()));

        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpaceByHandle(testSpace.getHandle().getSpace().getHandle()));
    }

    @Test
    public void removeSpaceByIdTest() {
        assertTrue(handleManager.removeSpaceById(TEST_SPACE));

        assertEquals(Set.of(otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpaceById(TEST_SPACE));

        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpaceById(HandleConstants.SPACES_ID));
    }

    @Test
    public void removeSpacesTest() {
        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpaces(Set.of(testSpace, testSpace.getHandle().getSpace())));

        assertEquals(Set.of(testSpace, otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertTrue(handleManager.removeSpaces(Set.of(testSpace, otherSpace)));

        assertEquals(Set.of(testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpaces(Set.of(testSpace, otherSpace)));
    }

    @Test
    public void removeSpacesByHandleTest() {
        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpacesByHandle(Set.of(testSpace.getHandle(), testSpace.getHandle().getSpace().getHandle())));

        assertEquals(Set.of(testSpace, otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertTrue(handleManager.removeSpacesByHandle(Set.of(testSpace.getHandle(), otherSpace.getHandle())));

        assertEquals(Set.of(testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpacesByHandle(Set.of(testSpace.getHandle(), otherSpace.getHandle())));
    }

    @Test
    public void removeSpacesByIdTest() {
        assertThrows(IllegalArgumentException.class, () -> handleManager.removeSpacesById(Set.of(TEST_SPACE, HandleConstants.SPACES_ID)));

        assertEquals(Set.of(testSpace, otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertTrue(handleManager.removeSpacesById(Set.of(TEST_SPACE, OTHER_SPACE)));

        assertEquals(Set.of(testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertFalse(handleManager.removeSpacesById(Set.of(TEST_SPACE, OTHER_SPACE)));
    }

    @Test
    public void spaceStreamTest() {
        assertEquals(Set.of(testSpace, otherSpace, testSpace.getHandle().getSpace(), testTag.getSpace()), handleManager.spaceStream().collect(Collectors.toSet()));
    }

    @Test
    public void getTagTest() {
        assertEquals(testTag, handleManager.getTag(TEST_TAG));
    }

    @Test
    public void getTagsTest() {
        assertEquals(Set.of(testTag, otherTag), Set.copyOf(handleManager.getTags()));
    }

    @Test
    public void createTagTest() {
        assertEquals(TAG_ID, handleManager.createTag(TAG_ID).getId());

        assertTrue(handleManager.containsTagById(TAG_ID));

        assertThrows(IllegalArgumentException.class, () -> handleManager.createTag(TAG_ID));
    }

    @Test
    public void getOrCreateTagTest() {
        assertEquals(TAG_ID, handleManager.getOrCreateTag(TAG_ID).getId());

        assertTrue(handleManager.containsTagById(TAG_ID));

        assertEquals(TAG_ID, handleManager.getOrCreateTag(TAG_ID).getId());
    }

    @Test
    public void containsTagTest() {
        assertTrue(handleManager.containsTag(testTag));

        assertFalse(handleManager.containsTag(testHandle));
    }

    @Test
    public void containsTagByIdTest() {
        assertTrue(handleManager.containsTagById(TEST_TAG));

        assertFalse(handleManager.containsTagById(TAG_ID));
    }

    @Test
    public void containsTagsTest() {
        assertTrue(handleManager.containsTags(Set.of(testTag, otherTag)));

        assertFalse(handleManager.containsTags(Set.of(testTag, testHandle)));
    }

    @Test
    public void containsTagsByIdTest() {
        assertTrue(handleManager.containsTagsById(Set.of(TEST_TAG, OTHER_TAG)));

        assertFalse(handleManager.containsTagsById(Set.of(TEST_TAG, TAG_ID)));
    }

    @Test
    public void removeTagTest() {
        assertTrue(handleManager.removeTag(testTag));

        assertEquals(Set.of(otherTag), handleManager.getTags());

        assertFalse(handleManager.removeTag(testTag));
    }

    @Test
    public void removeTagByIdTest() {
        assertTrue(handleManager.removeTagById(TEST_TAG));

        assertEquals(Set.of(otherTag), handleManager.getTags());

        assertFalse(handleManager.removeTagById(TEST_TAG));
    }

    @Test
    public void removeTagsTest() {
        assertTrue(handleManager.removeTags(Set.of(testTag, testHandle)));

        assertEquals(Set.of(otherTag), handleManager.getTags());

        assertTrue(handleManager.removeTags(Set.of(testTag, otherTag)));

        assertEquals(Set.of(), handleManager.getTags());

        assertFalse(handleManager.removeTags(Set.of(testTag, otherTag)));
    }

    @Test
    public void removeTagsByIdTest() {
        assertTrue(handleManager.removeTagsById(Set.of(TEST_TAG, TEST_HANDLE)));

        assertEquals(Set.of(otherTag), handleManager.getTags());

        assertTrue(handleManager.removeTagsById(Set.of(TEST_TAG, OTHER_TAG)));

        assertEquals(Set.of(), handleManager.getTags());

        assertFalse(handleManager.removeTagsById(Set.of(TEST_TAG, OTHER_TAG)));
    }

    @Test
    public void tagStreamTest() {
        assertEquals(Set.of(testTag, otherTag), handleManager.tagStream().collect(Collectors.toSet()));
    }

    @Test
    public void clearTest() {
        handleManager.clear();

        assertEquals(Set.of(testSpace.getHandle().getSpace(), testTag.getSpace()), Set.copyOf(handleManager.getSpaces()));

        assertEquals(Set.of(), handleManager.getTags());
    }
}
