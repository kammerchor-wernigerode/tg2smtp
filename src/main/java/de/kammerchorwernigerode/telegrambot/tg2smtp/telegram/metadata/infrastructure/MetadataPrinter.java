package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MetadataPrinter implements Printer<Metadata> {

    private final @NonNull Printer<User> authorPrinter;
    private final @NonNull Printer<Instant> timestampPrinter;

    @Override
    public String print(@NonNull Metadata source, @NonNull Locale locale) {
        return ""
                + authorPrinter.print(source.getAuthor().orElse(null), locale)
                + " — "
                + timestampPrinter.print(source.getTimestamp(), locale);
    }
}
