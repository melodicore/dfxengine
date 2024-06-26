package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handled;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public interface NumberFormatter extends Handled {
    String format(BigDecimal number, TextFactory factory, TextConfiguration configuration);
}
