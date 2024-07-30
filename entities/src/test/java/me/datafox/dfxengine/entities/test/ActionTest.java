package me.datafox.dfxengine.entities.test;

import me.datafox.dfxengine.entities.action.CreateEntityAction;
import me.datafox.dfxengine.entities.action.RemoveEntityAction;
import me.datafox.dfxengine.entities.action.ValueMapOperationAction;
import me.datafox.dfxengine.entities.action.ValueOperationAction;
import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class ActionTest extends AbstractTest {
    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        engine.addSerializedPack(TestLoader.load("/actions"));
        engine.loadPacks(false);
    }

    @Test
    public void createEntityActionTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var action = component.getActions().get("create");
        assertFalse(engine.getEntities().containsKey("multi"));
        engine.update(1);
        assertFalse(engine.getEntities().containsKey("multi"));
        var handle = EntityHandles.getEntities().getOrCreateHandle("multi");
        action.schedule(new ActionParameters().put(CreateEntityAction.HANDLES, List.of(handle)));
        assertFalse(engine.getEntities().containsKey("multi"));
        engine.update(1);
        assertTrue(engine.getEntities().containsKey("multi"));
        assertEquals(1, engine.getEntities().get("multi").size());
        action.schedule(new ActionParameters().put(CreateEntityAction.HANDLES, List.of(handle, handle)));
        engine.update(1);
        var entities = engine.getEntities().get("multi");
        assertEquals(3, entities.size());
        assertNotEquals(entities.get(0), entities.get(1));
        assertNotEquals(entities.get(0), entities.get(2));
        assertNotEquals(entities.get(1), entities.get(2));
    }

    @Test
    public void removeEntityActionTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var action = component.getActions().get("remove");
        Handle handle = EntityHandles.getEntities().getOrCreateHandle("multi");
        engine.createMultiEntities(List.of(handle, handle, handle));
        var entities = engine.getEntities().get("multi");
        assertEquals(3, entities.size());
        var multi1 = entities.get(0);
        var multi2 = entities.get(1);
        var multi3 = entities.get(2);
        assertNotEquals(multi1, multi2);
        assertNotEquals(multi1, multi3);
        assertNotEquals(multi2, multi3);
        action.schedule(new ActionParameters().put(RemoveEntityAction.ENTITIES, List.of(multi2)));
        engine.update(1);
        entities = engine.getEntities().get("multi");
        assertEquals(2, entities.size());
        assertEquals(multi1, entities.get(0));
        assertEquals(multi3, entities.get(1));
    }

    @Test
    public void valueMapOperationActionTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var action = component.getActions().get("map");
        var map = component.getData(ValueMap.class).get("map").getData();
        var value1 = map.get("value1");
        var value2 = map.get("value2");
        assertTrue(value1.compare(Comparison.equal(), Numerals.of(69)));
        assertTrue(value2.compare(Comparison.equal(), Numerals.of(420)));
        action.schedule(new ActionParameters().put(ValueMapOperationAction.OUTPUTS, List.of(map)));
        engine.update(1);
        assertTrue(value1.compare(Comparison.equal(), Numerals.of(70)));
        assertTrue(value2.compare(Comparison.equal(), Numerals.of(421)));
    }

    @Test
    public void valueOperationActionTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var action = component.getActions().get("value");
        var map = component.getData(ValueMap.class).get("map").getData();
        var value = component.getData(Value.class).get("value").getData();
        var value1 = map.get("value1");
        var value2 = map.get("value2");
        assertTrue(value1.compare(Comparison.equal(), Numerals.of(69)));
        assertTrue(value2.compare(Comparison.equal(), Numerals.of(420)));
        assertTrue(value.compare(Comparison.equal(), Numerals.of(5)));
        action.schedule(new ActionParameters().put(ValueOperationAction.OUTPUTS, List.of(value, value1)));
        engine.update(1);
        assertTrue(value1.compare(Comparison.equal(), Numerals.of(70)));
        assertTrue(value2.compare(Comparison.equal(), Numerals.of(420)));
        assertTrue(value.compare(Comparison.equal(), Numerals.of(6)));
    }
}
