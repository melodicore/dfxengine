package me.datafox.dfxengine.handles.test;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class HandleTest extends AbstractTest {
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
        assertEquals(Set.of(testTag, otherTag), testHandle.getTags());
    }

    @Test
    public void addTagTest() {
        var createdTag = handleManager.createTag(TAG_ID);

        assertTrue(testHandle.addTag(createdTag));

        assertEquals(Set.of(testTag, otherTag, createdTag), testHandle.getTags());

        assertFalse(testHandle.addTag(createdTag));
    }

    @Test
    public void addTagByIdTest() {
        assertTrue(testHandle.addTagById(TAG_ID));

        var tag = handleManager.getTag(TAG_ID);

        assertEquals(Set.of(testTag, otherTag, tag), testHandle.getTags());

        assertFalse(testHandle.addTagById(TAG_ID));
    }

    @Test
    public void addTagsTest() {
        var createdTag = handleManager.createTag(TAG_ID);

        var anotherTag = handleManager.createTag(ANOTHER_TAG);

        assertTrue(testHandle.addTags(Set.of(testTag, createdTag, anotherTag)));

        assertEquals(Set.of(testTag, otherTag, createdTag, anotherTag), testHandle.getTags());

        assertFalse(testHandle.addTags(Set.of(testTag, createdTag, anotherTag)));
    }

    @Test
    public void addTagsByIdTest() {
        assertTrue(testHandle.addTagsById(Set.of(TAG_ID, OTHER_TAG_ID)));

        assertEquals(TAG_ID, handleManager.getTag(TAG_ID).getId());

        assertEquals(OTHER_TAG_ID, handleManager.getTag(OTHER_TAG_ID).getId());

        assertFalse(testHandle.addTagsById(Set.of(TAG_ID, OTHER_TAG_ID)));
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
        assertTrue(testHandle.containsTags(Set.of(testTag, otherTag)));

        assertTrue(testHandle.containsTags(Set.of(testTag)));

        assertFalse(testHandle.containsTags(Set.of(handleManager.createTag(TAG_ID))));

        assertFalse(testHandle.containsTags(Set.of(testTag, handleManager.getTag(TAG_ID))));
    }

    @Test
    public void containsTagsByIdTest() {
        assertTrue(testHandle.containsTagsById(Set.of(TEST_TAG, OTHER_TAG)));

        assertTrue(testHandle.containsTagsById(Set.of(TEST_TAG)));

        assertFalse(testHandle.containsTagsById(Set.of(TAG_ID)));

        assertFalse(testHandle.containsTagsById(Set.of(TEST_TAG, TAG_ID)));
    }

    @Test
    public void removeTagTest() {
        assertTrue(testHandle.removeTag(testTag));

        assertEquals(Set.of(otherTag), testHandle.getTags());

        assertFalse(testHandle.removeTag(testTag));
    }

    @Test
    public void removeTagByIdTest() {
        assertTrue(testHandle.removeTagById(TEST_TAG));

        assertEquals(Set.of(otherTag), testHandle.getTags());

        assertFalse(testHandle.removeTagById(TEST_TAG));
    }

    @Test
    public void removeTagsTest() {
        assertTrue(testHandle.removeTags(Set.of(testTag, testHandle)));

        assertEquals(Set.of(otherTag), testHandle.getTags());

        assertTrue(testHandle.removeTags(Set.of(testTag, otherTag)));

        assertEquals(Set.of(), testHandle.getTags());

        assertFalse(testHandle.removeTags(Set.of(testTag, otherTag)));
    }

    @Test
    public void removeTagsByIdTest() {
        assertTrue(testHandle.removeTagsById(Set.of(TEST_TAG, TAG_ID)));

        assertEquals(Set.of(otherTag), testHandle.getTags());

        assertTrue(testHandle.removeTagsById(Set.of(TEST_TAG, OTHER_TAG)));

        assertEquals(Set.of(), testHandle.getTags());

        assertFalse(testHandle.removeTagsById(Set.of(TEST_TAG, OTHER_TAG)));
    }

    @Test
    public void tagStreamTest() {
        assertEquals(Set.of(testTag, otherTag), testHandle.tagStream().collect(Collectors.toSet()));
    }

    @Test
    public void clearTagsTest() {
        testHandle.clearTags();

        assertEquals(Set.of(), testHandle.getTags());
    }
}
