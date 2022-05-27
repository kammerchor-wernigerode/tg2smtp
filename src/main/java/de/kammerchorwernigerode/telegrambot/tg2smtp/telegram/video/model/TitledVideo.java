package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.video.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Optional;

/**
 * VO that encapsulates a {@link Video Telegram video file} and its caption.
 *
 * @author Vincent Nadoll
 */
@Value
public class TitledVideo {

    @Nullable String caption;
    @NonNull Video content;

    public Optional<String> getCaption() {
        return Optional.ofNullable(caption);
    }
}
