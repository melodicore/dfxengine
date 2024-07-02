package me.datafox.dfxengine.entities.system;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.DelegatedValueMap;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.MapMathContext;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class ValueMapOperationSystem extends AbstractEntitySystem {
    private final Space space;
    private final List<ValueMap> inputs;
    private final List<ValueMap> outputs;
    private final SingleParameterOperation operation;
    private final MapMathContext context;

    public ValueMapOperationSystem(int priority, List<ValueMap> inputs, List<ValueMap> outputs, SingleParameterOperation operation, MapMathContext context) {
        super(priority);
        this.inputs = inputs;
        this.outputs = outputs;
        this.operation = operation;
        if(context != null) {
            this.context = context;
        } else {
            this.context = MapMathContext.defaults();
        }
        if(inputs.isEmpty()) {
            throw new IllegalArgumentException("empty inputs");
        }
        if(outputs.isEmpty()) {
            throw new IllegalArgumentException("empty outputs");
        }
        space = inputs.get(0).getSpace();
        if(!Stream.concat(inputs.stream(), outputs.stream()).map(ValueMap::getSpace).allMatch(Predicate.isEqual(space))) {
            throw new IllegalArgumentException("all maps must have the same space");
        }
    }

    @Override
    public void update(Engine engine, float delta) {
        ValueMap combined = new DelegatedValueMap(new HashHandleMap<>(space), false, engine.getLogger());
        inputs.forEach(i -> combined.apply(Operations::add, MapMathContext.builder().createNonExistingAs(Numerals.of(0)).build(), i.getValueNumeralMap()));
        if(delta != 1) {
            combined.apply(Operations::multiply, Numerals.of(delta));
        }
        outputs.forEach(o -> o.apply(operation, context, combined.getValueNumeralMap()));
    }
}
