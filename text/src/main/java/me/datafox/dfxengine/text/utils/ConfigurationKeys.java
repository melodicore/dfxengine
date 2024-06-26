package me.datafox.dfxengine.text.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.NumberSuffixFactory;

/**
 * @author datafox
 */
@Component
public class ConfigurationKeys {
    /**
     * General delimiter. The default value is <code>&nbsp;</code> (a single whitespace).
     */
    public static final ConfigurationKey<String> DELIMITER = ConfigurationKey.of(" ");

    /**
     * Delimiter used for lists before the second to last element. The default value is {@code , }.
     */
    public static final ConfigurationKey<String> LIST_DELIMITER = ConfigurationKey.of(", ");

    /**
     * Delimiter used for lists between the second to last and the last element. The default value is
     * <code>&nbsp;and </code>.
     */
    public static final ConfigurationKey<String> LIST_LAST_DELIMITER = ConfigurationKey.of(" and ");

    /**
     * {@link Handle} of the {@link NumberFormatter} to be used. The default value is
     * {@link TextHandles#getSimpleNumberFormatter()}.
     */
    public static final ConfigurationKey<Handle> NUMBER_FORMATTER = ConfigurationKey.of();

    /**
     * {@link Handle} of the {@link NumberSuffixFactory} used by all {@link NumberFormatter NumberFormatters}. The
     * default value is {@link TextHandles#getExponentSuffixFactory()}.
     */
    public static final ConfigurationKey<Handle> NUMBER_SUFFIX_FACTORY = ConfigurationKey.of();

    @Inject
    private ConfigurationKeys(TextHandles handles) {
        NUMBER_FORMATTER.setDefaultValue(handles.getSimpleNumberFormatter());
        NUMBER_SUFFIX_FACTORY.setDefaultValue(handles.getExponentSuffixFactory());
    }
}
