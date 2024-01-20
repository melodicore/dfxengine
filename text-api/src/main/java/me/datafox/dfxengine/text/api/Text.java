package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;

/**
 * A text value that can depend on other text values.
 *
 * @author datafox
 */
public interface Text extends CharSequence, Dependency, Dependent {
    /**
     * @return {@link String} representation of this text
     */
    String getText();

    @Override
    default int length() {
        return getText().length();
    }

    @Override
    default char charAt(int index) {
        return getText().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return getText().subSequence(start, end);
    }
}
