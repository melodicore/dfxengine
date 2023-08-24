package me.datafox.dfxengine.handles.test;

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
public class HandleTest {
    private static HandleManager handleManager;

    @BeforeAll
    public static void beforeAll() {
        handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
    }

    private Space testSpace;

    private Handle testHandle;

    private Handle testTag;

    private Handle otherTestTag;

    @BeforeEach
    public void beforeEach() {
        handleManager.clear();

        testSpace = handleManager.createSpace(TEST_SPACE);
        testHandle = testSpace.createHandle(TEST_HANDLE);

        testTag = handleManager.createTag(TEST_TAG);
        otherTestTag = handleManager.createTag(OTHER_TEST_TAG);

        testHandle.addTag(testTag);
        testHandle.addTag(otherTestTag);
    }

    @Test
    public void getHandleManagerTest() {
        assertEquals(handleManager, testHandle.getHandleManager());
    }

    @Test
    public void getSpaceTest() {
        assertEquals(testSpace, testHandle.getSpace());
    }

    @Test
    public void getIdTest() {
        assertEquals(TEST_HANDLE, testHandle.getId());
    }

    @Test
    public void getIndexTest() {
        assertEquals(0, testHandle.getIndex());
    }

    @Test
    public void isIdTest() {
        assertTrue(testHandle.isId(TEST_HANDLE));

        assertFalse(testHandle.isId(TEST_SPACE));
    }

    @Test
    public void getTagsTest() {
        assertEquals(Set.of(testTag, otherTestTag), testHandle.getTags());
    }

    @Test
    public void addTagTest() {
        var otherTag = handleManager.createTag(OTHER_TAG);

        assertTrue(testHandle.addTag(otherTag));

        assertFalse(testHandle.addTag(otherTag));

        assertEquals(Set.of(testTag, otherTestTag, otherTag), testHandle.getTags());
    }

    @Test
    public void addTagByIdTest() {
        assertTrue(testHandle.addTagById(TAG_ID));

        assertFalse(testHandle.addTagById(TAG_ID));

        var tag = handleManager.getTag(TAG_ID);

        assertEquals(Set.of(testTag, otherTestTag, tag), testHandle.getTags());
    }

    @Test
    public void addTagsTest() {
        var otherTag = handleManager.createTag(OTHER_TAG);

        var anotherTag = handleManager.createTag(ANOTHER_TAG);

        assertTrue(testHandle.addTags(Set.of(testTag, otherTag, anotherTag)));

        assertFalse(testHandle.addTags(Set.of(testTag, otherTag, anotherTag)));

        assertEquals(Set.of(testTag, otherTestTag, otherTag, anotherTag), testHandle.getTags());
    }

    @Test
    public void addTagsByIdTest() {
        assertTrue(testHandle.addTagsById(Set.of(TAG_ID, OTHER_TAG_ID)));

        assertFalse(testHandle.addTagsById(Set.of(TAG_ID, OTHER_TAG_ID)));

        assertEquals(TAG_ID, handleManager.getTag(TAG_ID).getId());

        assertEquals(OTHER_TAG_ID, handleManager.getTag(OTHER_TAG_ID).getId());
    }

    @Test
    public void containsTagTest() {
        assertTrue(testHandle.containsTag(testTag));

        assertFalse(testHandle.containsTag(handleManager.createTag(TAG_ID)));
    }

    @Test
    public void containsTagByIdTest() {
        assertTrue(testHandle.containsTagById(TEST_TAG));

        assertFalse(testHandle.containsTagById(TAG_ID));
    }

    @Test
    public void containsTagsTest() {
        assertTrue(testHandle.containsTags(Set.of(testTag, otherTestTag)));

        assertTrue(testHandle.containsTags(Set.of(testTag)));

        assertFalse(testHandle.containsTags(Set.of(handleManager.createTag(TAG_ID))));

        assertFalse(testHandle.containsTags(Set.of(testTag, handleManager.getTag(TAG_ID))));
    }

    @Test
    public void containsTagsByIdTest() {
        assertTrue(testHandle.containsTagsById(Set.of(TEST_TAG, OTHER_TEST_TAG)));

        assertTrue(testHandle.containsTagsById(Set.of(TEST_TAG)));

        assertFalse(testHandle.containsTagsById(Set.of(TAG_ID)));

        assertFalse(testHandle.containsTagsById(Set.of(TEST_TAG, TAG_ID)));
    }

    @Test
    public void removeTagTest() {
        assertTrue(testHandle.removeTag(testTag));

        assertFalse(testHandle.removeTag(testTag));

        assertEquals(Set.of(otherTestTag), testHandle.getTags());
    }

    @Test
    public void removeTagByIdTest() {
        assertTrue(testHandle.removeTagById(TEST_TAG));

        assertFalse(testHandle.removeTagById(TEST_TAG));

        assertEquals(Set.of(otherTestTag), testHandle.getTags());
    }

    @Test
    public void removeTagsTest() {
        assertTrue(testHandle.removeTags(Set.of(testTag, otherTestTag)));

        assertFalse(testHandle.removeTags(Set.of(testTag, otherTestTag)));

        assertEquals(Set.of(), testHandle.getTags());
    }

    @Test
    public void removeTagsByIdTest() {
        assertTrue(testHandle.removeTagsById(Set.of(TEST_TAG, OTHER_TEST_TAG)));

        assertFalse(testHandle.removeTagsById(Set.of(TEST_TAG, OTHER_TEST_TAG)));

        assertEquals(Set.of(), testHandle.getTags());
    }

    @Test
    public void tagStreamTest() {
        assertEquals(Set.of(testTag, otherTestTag), testHandle.tagStream().collect(Collectors.toSet()));
    }

    @Test
    public void clearTagsTest() {
        testHandle.clearTags();

        assertEquals(Set.of(), testHandle.getTags());
    }
}
