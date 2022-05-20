package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import lombok.NonNull;

import java.util.Locale;

/**
 * Generic type based printer for easy use.
 *
 * @author Vincent Nadoll
 */
public interface PrinterService {

    @org.springframework.lang.NonNull
    <T> String print(@NonNull T object);

    @org.springframework.lang.NonNull
    default <T> String print(@NonNull T object, @NonNull Locale locale) {
        return print(object);
    }
}
