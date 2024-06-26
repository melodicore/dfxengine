package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;

import java.util.List;

/**
 * @author datafox
 */
public abstract class CombinedText extends AbstractText {
    protected final List<Text> texts;

    protected CombinedText(List<Text> texts, TextConfiguration configuration) {
        super(configuration);
        this.texts = texts;
    }
}
