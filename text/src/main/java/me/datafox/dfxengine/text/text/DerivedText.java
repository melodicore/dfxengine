package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.text.api.Formatter;
import org.slf4j.LoggerFactory;

/**
 * @author datafox
 */
public final class DerivedText<T extends Dependency> extends DependencyDependentText {
    private final T object;
    private final Formatter<T> formatter;

    /**
     * @param object object for the text to be derived from
     * @param formatter formatter to derive the text from, or {@code null} if {@link Object#toString()} should be used
     * instead
     */
    public DerivedText(T object, Formatter<T> formatter) {
        super(LoggerFactory.getLogger(DerivedText.class), false);
        this.object = object;
        this.formatter = formatter;

        addDependency(object);
    }

    /**
     * The text will be derived with {@link Object#toString()} if this constructor is used.
     *
     * @param object object for the text to be derived from
     */
    public DerivedText(T object) {
        this(object, null);
    }

    @Override
    protected String calculateText() {
        if(formatter == null) {
            return object.toString();
        } else {
            return formatter.format(object);
        }
    }
}
