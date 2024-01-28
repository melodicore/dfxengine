package me.datafox.dfxengine.text.definitions;

import lombok.AllArgsConstructor;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.DELIMITER;

/**
 * @author datafox
 */
public class ParameterizedTextDefinitionChain implements TextDefinition {
    private final Definition[] definitions;

    private ParameterizedTextDefinitionChain(List<Definition> definitions) {
        this.definitions = definitions.toArray(Definition[]::new);
    }

    @Override
    public String getText(TextFactory factory, TextContext context) {
        String prefix = "";
        String delimiter = context.get(DELIMITER);
        StringBuilder sb = new StringBuilder();
        for(Definition definition : definitions) {
            definition.manipulator.accept(context);
            sb.append(prefix).append(definition.definition.getText(factory, context));
            prefix = delimiter;
        }
        return sb.toString();
    }

    /**
     * @return {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Definition> definitions;

        private Builder() {
            definitions = new ArrayList<>();
        }

        public Builder definition(TextDefinition definition) {
            return definition(definition, null);
        }

        public <T> Builder definition(TextDefinition definition, ContextManipulator manipulator) {
            definitions.add(new Definition(definition, manipulator));
            return this;
        }

        public Builder clearDefinitions() {
            definitions.clear();
            return this;
        }

        public ParameterizedTextDefinitionChain build() {
            return new ParameterizedTextDefinitionChain(definitions);
        }
    }

    @FunctionalInterface
    public interface ContextManipulator extends Consumer<TextContext> {}

    @AllArgsConstructor
    private static class Definition {
        private final TextDefinition definition;

        private final ContextManipulator manipulator;
    }
}
