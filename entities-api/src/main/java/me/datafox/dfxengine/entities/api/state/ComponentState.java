package me.datafox.dfxengine.entities.api.state;

import java.util.List;

/**
 * @author datafox
 */
public interface ComponentState {
    String getHandle();

    List<DataState> getData();
}
