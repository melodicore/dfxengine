package me.datafox.dfxengine.text.definition;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextDefinition;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.ArrayList;
import java.util.List;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.DELIMITER;

/**
 * @author datafox
 */
public class TextDefinitionChain implements TextDefinition {
    private final TextDefinition[] definitions;

    public TextDefinitionChain(List<? extends TextDefinition> definitions) {
        this.definitions = definitions.toArray(TextDefinition[]::new);
    }

    @Override
    public String getText(TextFactory factory, TextContext context) {
        String prefix = "";
        String delimiter = context.get(DELIMITER);
        StringBuilder sb = new StringBuilder();
        for(TextDefinition definition : definitions) {
            sb.append(prefix).append(definition.getText(factory, context));
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
        private final List<TextDefinition> definitions;

        private Builder() {
            definitions = new ArrayList<>();
        }

        public Builder definition(TextDefinition definition) {
            definitions.add(definition);
            return this;
        }

        public Builder definitions(List<? extends TextDefinition> definitions) {
            this.definitions.addAll(definitions);
            return this;
        }

        public Builder clearDefinitions() {
            definitions.clear();
            return this;
        }

        public TextDefinitionChain build() {
            return new TextDefinitionChain(definitions);
        }
    }
}
