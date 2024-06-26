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
 * @author datafox
 */
public class ChainedText extends CombinedText {
    /**
     * If {@code true}, {@link ConfigurationKeys#LIST_DELIMITER} and {@link ConfigurationKeys#LIST_LAST_DELIMITER} will
     * be used instead of {@link ConfigurationKeys#DELIMITER}. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> USE_LIST_DELIMITER = ConfigurationKey.of(false);

    public ChainedText(List<Text> texts, TextConfiguration configuration) {
        super(texts, configuration);
    }

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
