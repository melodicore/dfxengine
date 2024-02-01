package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;

/**
 * @author datafox
 */
@Builder
@Data
public class PrioritizedRunnable implements Runnable, Comparable<PrioritizedRunnable> {
    private final int priority;

    private final Runnable delegate;

    @Override
    public int compareTo(PrioritizedRunnable o) {
        return Integer.compare(priority, o.priority);
    }

    @Override
    public void run() {
        delegate.run();
    }
}
