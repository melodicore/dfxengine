package me.datafox.dfxengine.text.api;

/**
 * @author datafox
 */
public interface TextProcessor extends Comparable<TextProcessor> {
    String process(String str, TextFactory factory, TextContext context);

    int priority();

    @Override
    default int compareTo(TextProcessor o) {
        return Integer.compare(priority(), o.priority());
    }
}
