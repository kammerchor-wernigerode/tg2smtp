package de.kammerchorwernigerode.telegrambot.tg2smtp.format.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Printer printing an empty string in case the argument is {@literal null}.
 *
 * @author Vincent Nadoll
 */
public class NullSafePrinterDecorator<T> extends PrinterDecorator<T> implements Printer<T> {

    public NullSafePrinterDecorator(Printer<T> delegate) {
        super(delegate);
    }

    @NonNull
    @Override
    public String print(@Nullable T object) {
        return Optional.ofNullable(object)
                .map(super::print)
                .orElse("");
    }

    @NonNull
    @Override
    public String print(@Nullable T object, @Nullable Locale locale) {
        if (anyNull(object, locale)) {
            return "";
        }

        return super.print(object, locale);
    }

    private static boolean anyNull(Object... objects) {
        return Arrays.stream(objects).anyMatch(Objects::isNull);
    }
}
