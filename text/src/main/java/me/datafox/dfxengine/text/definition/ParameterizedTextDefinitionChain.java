package me.datafox.dfxengine.text.definition;

import lombok.AllArgsConstructor;
import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextContextManipulator;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.ArrayList;
import java.util.List;

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
            definition.before.accept(context);
            sb.append(prefix).append(definition.definition.getText(factory, context));
            definition.after.accept(context);
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
            return definition(definition, null, null);
        }

        public <T> Builder definition(TextDefinition definition, TextContextManipulator before) {
            definitions.add(new Definition(definition, before, null));
            return this;
        }

        public <T> Builder definition(TextDefinition definition, TextContextManipulator before, TextContextManipulator after) {
            definitions.add(new Definition(definition, before, after));
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

    @AllArgsConstructor
    private static class Definition {
        private final TextDefinition definition;

        private final TextContextManipulator before;

        private final TextContextManipulator after;
    }
}
