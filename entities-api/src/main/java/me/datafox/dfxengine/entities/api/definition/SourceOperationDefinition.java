package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

/**
 * @author datafox
 */
public interface SourceOperationDefinition extends OperationDefinition {
    @Override
    SourceOperation build(Engine engine);

    @Override
    default int getParameterCount() {
        return 0;
    }
}
