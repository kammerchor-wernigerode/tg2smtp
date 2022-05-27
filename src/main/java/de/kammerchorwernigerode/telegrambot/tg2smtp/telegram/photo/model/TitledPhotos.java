package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * VO that encapsulates a {@link Photos Telegram photos} and its caption.
 *
 * @author Vincent Nadoll
 */
@Value
public class TitledPhotos {

    @Nullable String caption;
    @NonNull Photos content;

    public Optional<String> getCaption() {
        return Optional.ofNullable(caption);
    }
}
