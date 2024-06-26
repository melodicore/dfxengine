package me.datafox.dfxengine.text.api;

import lombok.Data;
import me.datafox.dfxengine.handles.api.Handled;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public interface NumberSuffixFactory extends Handled {
    Output format(BigDecimal number, TextFactory factory, TextConfiguration configuration);

    boolean isInfinite();

    @Data
    class Output {
        private final BigDecimal scaled;

        private final String suffix;

        private final int exponent;
    }
}
