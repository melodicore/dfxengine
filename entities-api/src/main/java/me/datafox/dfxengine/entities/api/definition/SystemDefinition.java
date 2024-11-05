package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.math.api.NumeralType;

import java.util.List;

/**
 * @author datafox
 */
public interface SystemDefinition {
    NumeralType getIntervalType();

    String getIntervalValue();

    List<NodeTreeDefinition> getTrees();
}
