package me.datafox.dfxengine.entities.component;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.component.SerializationHandler;
import me.datafox.dfxengine.entities.api.component.SerializationHandlerConfiguration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author datafox
 */
@Getter
public abstract class SerializationHandlerBase<B, T, H extends SerializationHandlerBase<B, T, H>> implements SerializationHandler<B, T> {
    protected final T serializer;

    protected SerializationHandlerBase(Supplier<B> builderSupplier, Function<B, T> buildFunction, List<SerializationHandlerConfiguration<B, T, H>> configurations) {
        B builder = builderSupplier.get();
        for(SerializationHandlerConfiguration<B, T, H> configuration : configurations) {
            builder = configuration.configure(builder);
        }
        serializer = buildFunction.apply(builder);
    }
}
