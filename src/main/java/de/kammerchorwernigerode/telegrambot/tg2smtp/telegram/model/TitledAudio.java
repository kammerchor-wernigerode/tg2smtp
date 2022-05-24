package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.Optional;

/**
 * VO that encapsulates a {@link Audio Telegram audio file} and its caption.
 *
 * @author Vincent Nadoll
 */
@Value
public class TitledAudio {

    @Nullable String caption;
    @NonNull Audio content;

    public Optional<String> getCaption() {
        return Optional.ofNullable(caption);
    }
}
