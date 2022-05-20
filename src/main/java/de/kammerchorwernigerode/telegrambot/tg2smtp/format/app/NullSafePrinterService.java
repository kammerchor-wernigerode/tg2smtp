package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link PrinterService} that delegates behaviour to the given subject in case inputs aren't null.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class NullSafePrinterService implements PrinterService {

    private final PrinterService subject;

    @org.springframework.lang.NonNull
    @Override
    public <T> @NonNull String print(@Nullable T object) {
        return Optional.ofNullable(object)
                .map(subject::print)
                .orElse("");
    }

    @org.springframework.lang.NonNull
    @Override
    public <T> String print(@Nullable T object, @Nullable Locale locale) {
        if (anyNull(object, locale)) {
            return "";
        }

        return subject.print(object, locale);
    }

    private static boolean anyNull(Object... objects) {
        return Arrays.stream(objects).anyMatch(Objects::isNull);
    }
}
