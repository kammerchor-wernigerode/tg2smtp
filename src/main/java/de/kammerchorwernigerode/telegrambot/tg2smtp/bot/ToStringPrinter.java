package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Locale;
import java.util.Objects;

/**
 * Generic wildcard printer, primarily used for debugging.
 *
 * @author Vincent Nadoll
 */
public class ToStringPrinter implements Printer<Object> {

    @NonNull
    @Override
    public String print(@Nullable Object object) {
        return Objects.toString(object, "");
    }

    @NonNull
    @Override
    public String print(@Nullable Object object, @Nullable Locale locale) {
        return print(object);
    }
}
