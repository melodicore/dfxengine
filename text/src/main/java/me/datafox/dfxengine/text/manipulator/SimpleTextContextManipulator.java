package me.datafox.dfxengine.text.manipulator;

import me.datafox.dfxengine.text.api.TextContext;
import me.datafox.dfxengine.text.api.TextContextData;
import me.datafox.dfxengine.text.api.TextContextManipulator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author datafox
 */
public class SimpleTextContextManipulator implements TextContextManipulator {
    private final Map<TextContextData<?>,?> parameters;

    public SimpleTextContextManipulator(Map<TextContextData<?>,?> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void accept(TextContext context) {
        parameters.forEach((data, value) -> accept(context, data, value));
    }

    @SuppressWarnings("unchecked")
    private <T> void accept(TextContext context, TextContextData<T> data, Object value) {
        if(value == null) {
            context.remove(data);
        } else {
            context.set(data, (T) value);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<TextContextData<?>,Object> parameters;

        private Builder() {
            parameters = new HashMap<>();
        }

        public <T> Builder parameter(TextContextData<T> data, T value) {
            this.parameters.put(data, value);
            return this;
        }

        public Builder clearParameters() {
            parameters.clear();
            return this;
        }

        public SimpleTextContextManipulator build() {
            return new SimpleTextContextManipulator(parameters);
        }
    }
}
