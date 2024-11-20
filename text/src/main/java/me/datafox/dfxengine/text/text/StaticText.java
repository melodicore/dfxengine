package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextFactory;

/**
 * A {@link Text} implementation that returns a predetermined {@link String}. This {@link Text} does not use any
 * configuration.
 *
 * @author datafox
 */
public class StaticText implements Text {
    private final String text;

    /**
     * Public constructor for {@link StaticText}.
     *
     * @param text {@link String} to be returned by this text
     */
    public StaticText(String text) {
        this.text = text;
    }

    /**
     * Returns a {@link String}.
     *
     * @param factory ignored parameter
     * @param configuration ignored parameter
     * @return associated {@link String}
     */
    @Override
    public String get(TextFactory factory, Configuration configuration) {
        return text;
    }
}
