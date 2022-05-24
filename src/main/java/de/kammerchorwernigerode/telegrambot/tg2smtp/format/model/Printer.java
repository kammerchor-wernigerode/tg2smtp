package de.kammerchorwernigerode.telegrambot.tg2smtp.format.model;

import lombok.NonNull;

import java.util.Locale;

/**
 * Simpler version of Springs {@link org.springframework.format.Printer}.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface Printer<T> extends org.springframework.format.Printer<T> {

    @org.springframework.lang.NonNull
    String print(@NonNull T object);

    @org.springframework.lang.NonNull
    @Override
    default String print(@NonNull T object, @NonNull Locale locale) {
        return print(object);
    }
}
