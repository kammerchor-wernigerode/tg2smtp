package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public final class Metadata {

    private final Instant timestamp;
    private final User author;

    public Metadata(long epochSecond, @Nullable User author) {
        this.timestamp = Instant.ofEpochSecond(epochSecond);
        this.author = author;
    }

    public Optional<User> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Locale getLocale() {
        return getAuthor()
                .map(User::getLanguageCode)
                .map(Locale::forLanguageTag)
                .orElse(Locale.getDefault());
    }
}
