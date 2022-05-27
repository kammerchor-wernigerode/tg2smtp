package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.infrastructure;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Printer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequiredArgsConstructor
public class TimestampPrinter implements Printer<Instant> {

    @NonNull
    private final DateTimeFormatter formatter;

    @Override
    public String print(@NonNull Instant instant, @NonNull Locale locale) {
        DateTimeFormatter formatter = this.formatter.withLocale(locale);
        return formatter.format(instant);
    }
}
