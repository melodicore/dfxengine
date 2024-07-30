package me.datafox.dfxengine.entities.test;

import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class LinkTest extends AbstractTest {
    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        engine.addSerializedPack(TestLoader.load("/links"));
        engine.loadPacks(false);
    }

    @Test
    public void valueMapModifierLinkTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var map = component.getData(ValueMap.class).get("map").getData();
        var value1 = map.get("value1");
        var value2 = map.get("value2");
        assertEquals(69, value1.getBase().getNumber());
        assertEquals(666.0, value2.getBase().getNumber());
        assertEquals(76.34f, value1.getValue().getNumber());
        assertEquals(673.34, value2.getValue().getNumber());
        clearEngine();
        assertEquals(69, value1.getValue().getNumber());
        assertEquals(666.0, value2.getValue().getNumber());
        linkEngine();
        assertEquals(76.34f, value1.getValue().getNumber());
        assertEquals(673.34, value2.getValue().getNumber());
    }
    @Test

    public void valueModifierLinkTest() {
        var entity = engine.getEntities().get("entity").get(0);
        var component = entity.getComponents().get("component");
        var value = component.getData(Value.class).get("value").getData();
        assertEquals(1337.0f, value.getBase().getNumber());
        assertEquals(new BigDecimal("1342.25"), value.getValue().getNumber());
        clearEngine();
        assertEquals(1337.0f, value.getValue().getNumber());
        linkEngine();
        assertEquals(new BigDecimal("1342.25"), value.getValue().getNumber());
    }
}
