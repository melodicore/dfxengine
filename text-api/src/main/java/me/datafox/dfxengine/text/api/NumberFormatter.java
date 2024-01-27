package me.datafox.dfxengine.text.api;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface NumberFormatter extends Handled {
    Details format(Number number, TextFactory.Context context);

    @Data
    @Builder
    class Details {
        private final Number number;

        private final String string;

        private final boolean one;
    }
}
