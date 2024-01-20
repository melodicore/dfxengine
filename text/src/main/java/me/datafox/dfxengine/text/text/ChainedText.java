package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.utils.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public class ChainedText extends DependencyDependentText {
    private final List<Text> texts;
    private final String separator;
    private final String lastSeparator;

    /**
     * @param texts {@link Text Texts} for this text
     */
    public ChainedText(Collection<? extends Text> texts, String separator, String lastSeparator) {
        super(LoggerFactory.getLogger(ChainedText.class), false);
        this.texts = new ArrayList<>(texts);
        this.separator = separator;
        this.lastSeparator = lastSeparator;
    }

    @Override
    protected String calculateText() {
        return StringUtils.joining(texts, separator, lastSeparator);
    }
}
