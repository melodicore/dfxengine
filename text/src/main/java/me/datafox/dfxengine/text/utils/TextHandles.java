package me.datafox.dfxengine.text.utils;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.NumberSuffixFormatter;
import me.datafox.dfxengine.text.formatter.EvenLengthNumberFormatter;
import me.datafox.dfxengine.text.formatter.SimpleNumberFormatter;
import me.datafox.dfxengine.text.formatter.SplittingNumberFormatter;
import me.datafox.dfxengine.text.suffix.CharDigitSuffixFormatter;
import me.datafox.dfxengine.text.suffix.ExponentSuffixFormatter;
import me.datafox.dfxengine.text.suffix.NamedSuffixFormatter;

/**
 * Initializes {@link Space Spaces} and {@link Handle Handles} used by this module. This class is designed to be used
 * with the {@link Injector}.
 *
 * @author datafox
 */
@Getter
@Component
public class TextHandles {
    /**
     * {@link Space} for {@link Handle Handles} of {@link NumberFormatter NumberFormatters}.
     */
    private final Space numberFormatters;

    /**
     * {@link Handle} for the {@link SimpleNumberFormatter}.
     */
    private final Handle simpleNumberFormatter;

    /**
     * {@link Handle} for the {@link EvenLengthNumberFormatter}.
     */
    private final Handle evenLengthNumberFormatter;

    /**
     * {@link Handle} for the {@link SplittingNumberFormatter}.
     */
    private final Handle splittingNumberFormatter;

    /**
     * {@link Space} for {@link Handle Handles} of {@link NumberSuffixFormatter NumberSuffixFormatters}.
     */
    private final Space numberSuffixFormatters;

    /**
     * {@link Handle} for the {@link ExponentSuffixFormatter}.
     */
    private final Handle exponentSuffixFormatter;

    /**
     * {@link Handle} for the {@link NamedSuffixFormatter}.
     */
    private final Handle namedSuffixFormatter;

    /**
     * {@link Handle} for the {@link CharDigitSuffixFormatter}.
     */
    private final Handle charDigitSuffixFormatter;

    /**
     * @param handleManager {@link HandleManager} to be used for initializing the {@link Space Spaces} and
     * {@link Handle Handles}
     */
    @Inject
    public TextHandles(HandleManager handleManager) {
        numberFormatters = handleManager.getOrCreateSpace("numberFormatters");
        simpleNumberFormatter = numberFormatters.getOrCreateHandle("simpleNumberFormatter");
        evenLengthNumberFormatter = numberFormatters.getOrCreateHandle("evenLengthNumberFormatter");
        splittingNumberFormatter = numberFormatters.getOrCreateHandle("splittingNumberFormatter");
        numberSuffixFormatters = handleManager.getOrCreateSpace("numberSuffixFormatters");
        exponentSuffixFormatter = numberSuffixFormatters.getOrCreateHandle("exponentSuffixFormatter");
        namedSuffixFormatter = numberSuffixFormatters.getOrCreateHandle("namedSuffixFormatter");
        charDigitSuffixFormatter = numberSuffixFormatters.getOrCreateHandle("charDigitNumberFormatter");
    }
}
