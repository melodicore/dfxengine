package me.datafox.dfxengine.entities.definition.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.action.ValueMapOperationAction;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;
import me.datafox.dfxengine.entities.api.definition.MathContextDefinition;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapOperationActionDefinition implements ActionDefinition {
    private String handle;
    private OperationDefinition operation;
    private List<DataReference<Value>> inputs;
    private DataReference<ValueMap> outputs;
    private MathContextDefinition definition;

    @Override
    public ValueMapOperationAction build(Engine engine) {
        return new ValueMapOperationAction(EntityHandles.getActions().getOrCreateHandle(handle),
                operation.build(engine),
                EntityUtils.assertSingleAndStream(engine, inputs).collect(Collectors.toList()),
                outputs.get(engine).collect(Collectors.toList()), definition.build(engine));
    }
}
