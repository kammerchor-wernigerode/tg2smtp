package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Generic wildcard printer, primarily used for debugging.
 *
 * @author Vincent Nadoll
 */
public class ToStringPrinter implements Printer<Object> {

    @org.springframework.lang.NonNull
    @Override
    public String print(@Nullable Object object) {
        return Objects.toString(object, "");
    }
}
