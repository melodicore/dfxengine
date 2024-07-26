package me.datafox.dfxengine.entities.definition.modifier;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.definition.ModifierDefinition;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class OperationModifierDefinition implements ModifierDefinition {
    public int priority;
    public OperationDefinition operation;
    public List<Reference<Value>> inputs;

    @Builder
    public OperationModifierDefinition(int priority, OperationDefinition operation, @Singular List<Reference<Value>> inputs) {
        this.priority = priority;
        this.operation = operation;
        this.inputs = new ArrayList<>(inputs);
        if(inputs.size() != operation.getParameterCount()) {
            throw new IllegalArgumentException("input count does not match operation's parameter count");
        }
    }

    @Override
    public Modifier build(Engine engine) {
        return new OperationModifier(priority, operation.build(engine),
                EntityUtils.assertSingleAndStream(engine, inputs).toArray(Value[]::new));
    }
}
