package me.datafox.dfxengine.entities.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class StreamUtils {
    public static <T> Function<T, Stream<T>> distinctSame() {
        return new Function<>() {
            private final List<T> list = new ArrayList<>();

            @Override
            public Stream<T> apply(T t) {
                if(list.stream().anyMatch(e -> e == t)) {
                    return Stream.empty();
                }
                list.add(t);
                return Stream.of(t);
            }
        };
    }

    public static <T> Stream<T> reduceSame(Stream<T> t1, Stream<T> t2) {
        List<T> list = t1.collect(Collectors.toList());
        return t2.flatMap(t -> {
            if(list.stream().anyMatch(e -> e == t)) {
                return Stream.of(t);
            }
            return Stream.empty();
        });
    }
}
