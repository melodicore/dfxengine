package me.datafox.dfxengine.entities.definition.modifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationModifierDefinition implements ModifierDefinition {
    private int priority;
    private List<DataReference<Value>> inputs;
    private OperationDefinition operation;

    @Override
    public Modifier build(Engine engine) {
        return new OperationModifier(priority, operation.build(engine), parseInputs(engine));
    }

    private Value[] parseInputs(Engine engine) {
        List<Value> list = new ArrayList<>(inputs.size());
        for(DataReference<Value> input : inputs) {
            if(!input.isSingle()) {
                throw new IllegalArgumentException("OperationModifierDefinition inputs must be single DataReferences");
            }
            list.add(input.get(engine).findFirst().orElseThrow(() -> new IllegalArgumentException("Data not found")));
        }
        return list.toArray(Value[]::new);
    }
}
