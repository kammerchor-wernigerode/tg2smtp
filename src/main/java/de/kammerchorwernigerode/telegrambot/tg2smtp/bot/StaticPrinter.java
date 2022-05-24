package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.Printer;
import org.springframework.lang.Nullable;

import java.util.Locale;

import static lombok.AccessLevel.PRIVATE;

/**
 * {@link Printer} that statically returns the same independent of the input.
 *
 * @author Vincent Nadoll
 */
@NoArgsConstructor(access = PRIVATE)
public final class StaticPrinter<T> implements Printer<T> {

    @NonNull
    @Override
    public String print(@Nullable T object, @Nullable Locale locale) {
        return "";
    }

    public static <T> StaticPrinter<T> emptyString() {
        return (StaticPrinter<T>) Holder.INSTANCE;
    }

    /**
     * Utility that holds the instance of this.
     *
     * @author Vincent Nadoll
     */
    private static final class Holder {

        private static final StaticPrinter INSTANCE = new StaticPrinter<>();
    }
}
