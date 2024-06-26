package me.datafox.dfxengine.text.utils;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component
public class TextHandles {
    private final Space numberFormatters;
    private final Handle simpleNumberFormatter;
    private final Handle evenLengthNumberFormatter;
    private final Handle splittingNumberFormatter;
    private final Space numberSuffixFactories;
    private final Handle exponentSuffixFactory;
    private final Handle namedSuffixFactory;
    private final Handle charDigitSuffixFactory;

    @Inject
    public TextHandles(HandleManager handleManager) {
        numberFormatters = handleManager.getOrCreateSpace("numberFormatters");
        simpleNumberFormatter = numberFormatters.getOrCreateHandle("simpleNumberFormatter");
        evenLengthNumberFormatter = numberFormatters.getOrCreateHandle("evenLengthNumberFormatter");
        splittingNumberFormatter = numberFormatters.getOrCreateHandle("splittingNumberFormatter");
        numberSuffixFactories = handleManager.getOrCreateSpace("numberSuffixFactories");
        exponentSuffixFactory = numberSuffixFactories.getOrCreateHandle("exponentSuffixFactory");
        namedSuffixFactory = numberSuffixFactories.getOrCreateHandle("namedSuffixFactory");
        charDigitSuffixFactory = numberSuffixFactories.getOrCreateHandle("charDigitNumberFormatter");
    }
}
