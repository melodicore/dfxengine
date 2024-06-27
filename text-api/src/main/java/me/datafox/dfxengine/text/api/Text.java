package me.datafox.dfxengine.text.api;

/**
 * A {@link String} generation interface. {@link #get(TextFactory, TextConfiguration)} should use the provided
 * {@link TextConfiguration} and not {@link TextFactory#getConfiguration()}. The reference to {@link TextFactory} is
 * provided for access to objects like {@link Name Names} and {@link NumberFormatter NumberFormatters}.
 *
 * @author datafox
 */
@FunctionalInterface
public interface Text {
    /**
     * Implementation should use the provided {@link TextConfiguration} and not {@link TextFactory#getConfiguration()}.
     * The reference to {@link TextFactory} is provided for access to objects like {@link Name Names} and
     * {@link NumberFormatter NumberFormatters}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link TextConfiguration} for generation
     * @return generated {@link String}
     */
    String get(TextFactory factory, TextConfiguration configuration);
}
