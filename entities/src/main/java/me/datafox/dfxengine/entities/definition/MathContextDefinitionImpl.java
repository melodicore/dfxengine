package me.datafox.dfxengine.entities.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.MathContextDefinition;
import me.datafox.dfxengine.entities.utils.internal.EntityUtils;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.operation.MapMathContext;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MathContextDefinitionImpl implements MathContextDefinition {
    private String convertResultTo;
    private String ignoreBadConversion;
    private String convertToDecimal;
    private String createNonExistingAsType;
    private String createNonExistingAsValue;

    @Override
    public MapMathContext build(Engine engine) {
        MapMathContext.MapMathContextBuilder<?,?> builder = MapMathContext.builder();
        if(getConvertResultTo() != null) {
            builder.convertResultTo(NumeralType.valueOf(convertResultTo));
        }
        if(ignoreBadConversion != null) {
            builder.ignoreBadConversion(Boolean.parseBoolean(ignoreBadConversion));
        }
        if(convertToDecimal != null) {
            builder.convertToDecimal(Boolean.parseBoolean(convertToDecimal));
        }
        if(createNonExistingAsType != null && createNonExistingAsValue != null) {
            builder.createNonExistingAs(EntityUtils.getNumeral(createNonExistingAsType, createNonExistingAsValue));
        }
        return builder.build();
    }
}
