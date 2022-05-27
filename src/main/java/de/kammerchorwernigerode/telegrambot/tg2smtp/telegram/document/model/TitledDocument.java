package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.document.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.Optional;

/**
 * VO that encapsulates a {@link Document Telegram document message} and its caption.
 *
 * @author Vincent Nadoll
 */
@Value
public class TitledDocument {

    @Nullable String caption;
    @NonNull Document content;

    public Optional<String> getCaption() {
        return Optional.ofNullable(caption);
    }
}
