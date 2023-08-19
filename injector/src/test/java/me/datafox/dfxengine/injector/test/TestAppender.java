package me.datafox.dfxengine.injector.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class TestAppender extends ListAppender<ILoggingEvent> {
    public TestAppender() {
        super();
    }

    public List<ILoggingEvent> getForLevel(Level level) {
        return list.stream()
                .filter(event -> event.getLevel().isGreaterOrEqual(level))
                .collect(Collectors.toList());
    }
}
