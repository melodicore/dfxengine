package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.dependencies.DependencyDependent;
import me.datafox.dfxengine.text.api.Text;
import org.slf4j.Logger;

/**
 * An abstract class that combines {@link Text} with {@link DependencyDependent}. It is recommended to extend this class
 * for any texts that depends on other objects. {@link #calculateText()} should calculate the value of this text based
 * on all dependencies. If this text is a generator, {@link #generate()} should be overridden and
 * {@code super.generate()} should be called before the generation.
 *
 * @author datafox
 */
public abstract class DependencyDependentText extends DependencyDependent implements Text {
    private final boolean generator;
    private String text;
    private boolean recalculate;

    /**
     * @param logger {@link Logger} for this text
     * @param generator {@code true} if this text is generator
     */
    protected DependencyDependentText(Logger logger, boolean generator) {
        super(logger);
        this.generator = generator;
        this.text = calculateText();
        recalculate = false;
    }

    /**
     * @return {@link String} representation of this text at the time of execution
     */
    protected abstract String calculateText();

    /**
     * @return {@link String} representation of this text
     */
    @Override
    public String getText() {
        if(recalculate) {
            text = calculateText();
            recalculate = false;
        }
        return text;
    }

    /**
     * Invalidates this text to be recalculated.
     */
    @Override
    protected void onInvalidate() {
        recalculate = true;
    }
}
