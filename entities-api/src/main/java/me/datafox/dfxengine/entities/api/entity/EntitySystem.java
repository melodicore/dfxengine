package me.datafox.dfxengine.entities.api.entity;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
public interface EntitySystem extends NodeTreeOwner {
    Numeral getInterval();
}
