package me.datafox.dfxengine.entities.test;

import me.datafox.dfxengine.entities.reference.selector.*;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.HandleMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class SelectorTest extends AbstractTest {
    private HandleManager hm;
    private HandleMap<Integer> map;

    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        hm = injector.getComponent(HandleManager.class);
        var s = hm.getOrCreateSpace("test");
        var g1 = s.getOrCreateGroup("group1");
        var g2 = s.getOrCreateGroup("group2");
        var h1 = s.getOrCreateHandle("handle1");
        var h2 = s.getOrCreateHandle("handle2");
        var h3 = s.getOrCreateHandle("handle3");
        var h4 = s.getOrCreateHandle("handle4");
        var h5 = s.getOrCreateHandle("handle5");
        var h6 = s.getOrCreateHandle("handle6");
        h1.getTags().add("tag1");
        h1.getTags().add("tag2");
        h1.getTags().add("tag3");
        h2.getTags().add("tag1");
        h2.getTags().add("tag2");
        h3.getTags().add("tag2");
        h3.getTags().add("tag3");
        h4.getTags().add("tag1");
        h4.getTags().add("tag3");
        h5.getTags().add("tag4");
        g1.getHandles().addAll(List.of(h1, h2, h3, h4));
        g2.getHandles().addAll(List.of(h3, h4, h5, h6));
        map = new HashHandleMap<>(s);
        map.put(h1, 1);
        map.put(h2, 2);
        map.put(h3, 3);
        map.put(h4, 4);
        map.put(h5, 5);
        map.put(h6, 6);
    }

    @Test
    public void everySelectorTest() {
        var selector = new EverySelector();
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(6, set.size());
        assertEquals(Set.of(1, 2, 3, 4, 5, 6), set);
    }

    @Test
    public void groupSelectorTest() {
        var selector = new GroupSelector("group1");
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(4, set.size());
        assertEquals(Set.of(1, 2, 3, 4), set);
    }

    @Test
    public void handleSelectorTest() {
        var selector = new HandleSelector("handle2");
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(1, set.size());
        assertEquals(Set.of(2), set);
    }

    @Test
    public void multiHandleSelectorTest() {
        var selector = new MultiHandleSelector(List.of("handle2", "handle3", "handle6"));
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(3, set.size());
        assertEquals(Set.of(2, 3, 6), set);
    }

    @Test
    public void tagSelectorTest() {
        var selector = new TagSelector("tag1");
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(3, set.size());
        assertEquals(Set.of(1, 2, 4), set);
    }

    @Test
    public void multiTagSelectorTest() {
        var selector = new MultiTagSelector(List.of("tag2", "tag3"));
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(2, set.size());
        assertEquals(Set.of(1, 3), set);
    }

    @Test
    public void andSelectorTest() {
        var selector1 = new GroupSelector("group2");
        var selector2 = new TagSelector("tag3");
        var selector = new AndSelector(List.of(selector1, selector2));
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(2, set.size());
        assertEquals(Set.of(3, 4), set);
    }

    @Test
    public void orSelectorTest() {
        var selector1 = new GroupSelector("group2");
        var selector2 = new TagSelector("tag3");
        var selector = new OrSelector(List.of(selector1, selector2));
        var set = selector.select(map, engine).collect(Collectors.toSet());
        assertEquals(5, set.size());
        assertEquals(Set.of(1, 3, 4, 5, 6), set);
    }
}
