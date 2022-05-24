package de.kammerchorwernigerode.telegrambot.tg2smtp.common;

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
}
