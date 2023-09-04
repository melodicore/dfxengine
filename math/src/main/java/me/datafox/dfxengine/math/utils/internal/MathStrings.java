package me.datafox.dfxengine.math.utils.internal;

import me.datafox.dfxengine.handles.HandleManagerImpl;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.collection.HashHandleMap;
import me.datafox.dfxengine.math.api.ValueMap;
import me.datafox.dfxengine.math.api.operation.MathContext;
import me.datafox.dfxengine.math.utils.ConversionUtils;
import me.datafox.dfxengine.math.utils.OperationUtils;
import me.datafox.dfxengine.math.value.DelegatedValueMap;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public class MathStrings {
    static {
        HandleManager handleManager = new HandleManagerImpl(LoggerFactory.getLogger(HandleManager.class));
        ValueMap map = new DelegatedValueMap(new HashHandleMap<>(handleManager.getOrCreateSpace("testSpace")));
        map.apply(OperationUtils::add, ConversionUtils.toNumeral(1), MathContext.getDefaults());
        map.apply(OperationUtils::divReversed, ConversionUtils.toNumeral(10.5), MathContext.getDefaults());
    }
}
