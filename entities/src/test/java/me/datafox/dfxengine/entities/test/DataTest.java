package me.datafox.dfxengine.entities.test;

import me.datafox.dfxengine.entities.data.*;
import me.datafox.dfxengine.entities.definition.data.ImmutableValueDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ImmutableValueMapDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ValueDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ValueMapDataDefinition;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.math.utils.Numerals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class DataTest extends AbstractTest {
    @Test
    public void immutableValueDataTest() {
        var data = new ImmutableValueData(ImmutableValueDataDefinition
                .builder()
                .value(ValueDto.of("value", Numerals.of(666.666)))
                .build());
        assertEquals(666.666, data.getData().getValue().getNumber());
        assertSame(EntityHandles.getData().getHandles().get("value"), data.getHandle());
        assertThrows(UnsupportedOperationException.class, () -> data.getData().set(Numerals.of(9001)));
    }

    @Test
    public void immutableValueMapDataTest() {
        var space = injector.getComponent(HandleManager.class).getOrCreateSpace("valueSpace");
        var handle1 = space.getOrCreateHandle("value1");
        var handle2 = space.getOrCreateHandle("value2");
        var data = new ImmutableValueMapData(ImmutableValueMapDataDefinition
                .builder()
                .handle("map")
                .space("valueSpace")
                .value(ValueDto.of("value1", Numerals.of(525L)))
                .value(ValueDto.of("value2", Numerals.of(93.45f)))
                .build());
        assertEquals(525L, data.getData().get("value1").getValue().getNumber());
        assertEquals(93.45f, data.getData().get("value2").getValue().getNumber());
        assertSame(EntityHandles.getData().getHandles().get("map"), data.getHandle());
        assertSame(handle1, data.getData().get("value1").getHandle());
        assertSame(handle2, data.getData().get("value2").getHandle());
        assertThrows(UnsupportedOperationException.class, () -> data.getData().set(Numerals.of(9001)));
    }

    @Test
    public void valueDataTest() {
        var data = new ValueData(ValueDataDefinition
                .builder()
                .value(ValueDto.of("value", Numerals.of(666.666)))
                .build());
        assertEquals(666.666, data.getData().getValue().getNumber());
        assertSame(EntityHandles.getData().getHandles().get("value"), data.getHandle());
        var state = data.getState();
        assertDoesNotThrow(() -> data.getData().set(Numerals.of(9001)));
        assertEquals(9001, data.getData().getValue().getNumber());
        assertDoesNotThrow(() -> data.setState(state));
        assertEquals(666.666, data.getData().getValue().getNumber());
    }

    @Test
    public void valueMapDataTest() {
        var space = injector.getComponent(HandleManager.class).getOrCreateSpace("valueSpace");
        var handle1 = space.getOrCreateHandle("value1");
        var handle2 = space.getOrCreateHandle("value2");
        var data = new ValueMapData(ValueMapDataDefinition
                .builder()
                .handle("map")
                .space("valueSpace")
                .value(ValueDto.of("value1", Numerals.of(525L)))
                .value(ValueDto.of("value2", Numerals.of(93.45f)))
                .build());
        assertEquals(525L, data.getData().get("value1").getValue().getNumber());
        assertEquals(93.45f, data.getData().get("value2").getValue().getNumber());
        assertSame(EntityHandles.getData().getHandles().get("map"), data.getHandle());
        assertSame(handle1, data.getData().get("value1").getHandle());
        assertSame(handle2, data.getData().get("value2").getHandle());
        var state = data.getState();
        assertDoesNotThrow(() -> data.getData().set(Numerals.of(9001)));
        assertEquals(9001, data.getData().get("value1").getValue().getNumber());
        assertEquals(9001, data.getData().get("value2").getValue().getNumber());
        assertDoesNotThrow(() -> data.setState(state));
        assertEquals(525L, data.getData().get("value1").getValue().getNumber());
        assertEquals(93.45f, data.getData().get("value2").getValue().getNumber());
    }
}
