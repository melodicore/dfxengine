package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.TextUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.datafox.dfxengine.text.utils.ConfigurationKeys.*;

/**
 * A {@link Text} implementation that chains other {@link Text Texts} together by using either
 * {@link ConfigurationKeys#DELIMITER}, or {@link ConfigurationKeys#LIST_DELIMITER} and
 * {@link ConfigurationKeys#LIST_LAST_DELIMITER}, depending on the value of {@link #USE_LIST_DELIMITER}. If extra
 * {@link TextConfiguration} is provided, that will override the configuration provided to the
 * {@link #get(TextFactory, TextConfiguration)} method, but will be overridden by the configurations of the provided
 * {@link Text Texts}. However, the provided {@link Text Texts} will not affect the delimiter.
 *
 * @author datafox
 */
public class ChainedText extends AbstractText {
    /**
     * If {@code true}, {@link ConfigurationKeys#LIST_DELIMITER} and {@link ConfigurationKeys#LIST_LAST_DELIMITER} will
     * be used instead of {@link ConfigurationKeys#DELIMITER}. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> USE_LIST_DELIMITER = ConfigurationKey.of(false);

    private final List<Text> texts;

    /**
     * @param texts {@link Text Texts} to be chained together
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public ChainedText(List<Text> texts, TextConfiguration configuration) {
        super(configuration);
        this.texts = texts;
    }

    /**
     * @param texts {@link Text Texts} to be chained together
     */
    public ChainedText(List<Text> texts) {
        this(texts, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        Stream<String> stream = texts
                .stream()
                .map(t -> t.get(factory, configuration));
        if(configuration.get(USE_LIST_DELIMITER)) {
            return TextUtils.join(configuration.get(LIST_DELIMITER),
                    configuration.get(LIST_LAST_DELIMITER),
                    stream.collect(Collectors.toList()));
        } else {
            return stream.collect(Collectors.joining(configuration.get(DELIMITER)));
        }
    }
}
