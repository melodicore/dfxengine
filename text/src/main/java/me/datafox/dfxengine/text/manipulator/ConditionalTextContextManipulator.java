package me.datafox.dfxengine.text.manipulator;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextContextManipulator;

import java.util.function.Predicate;

/**
 * @author datafox
 */
public class ConditionalTextContextManipulator implements TextContextManipulator {
    private final Predicate<TextContext> condition;
    private final TextContextManipulator ifTrue;
    private final TextContextManipulator ifFalse;

    public ConditionalTextContextManipulator(Predicate<TextContext> condition, TextContextManipulator ifTrue, TextContextManipulator ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public ConditionalTextContextManipulator(Predicate<TextContext> condition, TextContextManipulator ifTrue) {
        this(condition, ifTrue, null);
    }

    @Override
    public void accept(TextContext context) {
        if(condition.test(context)) {
            if(ifTrue != null) {
                ifTrue.accept(context);
            }
        } else {
            if(ifFalse != null) {
                ifFalse.accept(context);
            }
        }
    }
}
