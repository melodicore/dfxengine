package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

/**
 * @author datafox
 */
public class StaticText implements Text {
    private final String text;

    public StaticText(String text) {
        this.text = text;
    }

    @Override
    public String get(TextFactory factory, TextConfiguration configuration) {
        return text;
    }
}
