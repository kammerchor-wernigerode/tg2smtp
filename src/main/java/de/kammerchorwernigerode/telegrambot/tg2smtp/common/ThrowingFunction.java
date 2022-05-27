package de.kammerchorwernigerode.telegrambot.tg2smtp.common;

import java.util.function.Function;

/**
 * A {@link java.util.function.Function} that may throw an exception.
 *
 * @param <T> the type of the input of the function
 * @param <R> the type of the output of the function
 * @param <E> the type of the exception this function may throw
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    R apply(T t) throws E;

    static <T, R, E extends Throwable> Function<T, R> sneaky(ThrowingFunction<T, R, E> subject) {
        return t -> {
            try {
                return subject.apply(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
