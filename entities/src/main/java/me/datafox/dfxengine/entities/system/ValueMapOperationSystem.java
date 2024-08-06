package me.datafox.dfxengine.entities.system;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.definition.system.ValueMapOperationSystemDefinition;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.DelegatedValueMap;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.operation.MapMathContext;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class ValueMapOperationSystem extends AbstractEntitySystem {
    private final ValueMapOperationSystemDefinition definition;
    private final SingleParameterOperation operation;
    private final List<ValueMap> inputs;
    private final List<ValueMap> outputs;
    private final MapMathContext context;
    private Space space;

    public ValueMapOperationSystem(ValueMapOperationSystemDefinition definition, Engine engine) {
        super(definition.getPriority(), engine);
        this.definition = definition;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        operation = definition.getOperation().build(engine);
        if(definition.getContext() != null) {
            context = definition.getContext().build(engine);
        } else {
            context = MapMathContext.defaults();
        }
    }

    @Override
    public void update(float delta) {
        ValueMap combined = new DelegatedValueMap(new HashHandleMap<>(space), false, getEngine().getLogger());
        inputs.forEach(i -> combined.apply(
                Operations::add,
                MapMathContext
                        .builder()
                        .createNonExistingAs(Numerals.of(0))
                        .build(),
                i.getValueNumeralMap()));
        if(delta != 1) {
            combined.apply(Operations::multiply, Numerals.of(delta));
        }
        outputs.forEach(o -> o.apply(operation, context, combined.getValueNumeralMap()));
    }

    @Override
    public void link() {
        inputs.addAll(definition.getInputs().get(getEngine()).collect(Collectors.toList()));
        outputs.addAll(definition.getOutputs().get(getEngine()).filter(this::checkImmutable).collect(Collectors.toList()));
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
    public void clear() {
        inputs.clear();
        outputs.clear();
        space = null;
    }

    private boolean checkImmutable(ValueMap map) {
        if(map.isImmutable()) {
            getEngine().getLogger()
                    .warn(String.format("%s is immutable and cannot be modified by ValueMapOperationSystem", map));
            return false;
        }
        return true;
    }
}
