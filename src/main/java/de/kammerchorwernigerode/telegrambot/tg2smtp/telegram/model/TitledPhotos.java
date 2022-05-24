package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Photos;
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
public class TitledPhotos {

    @Nullable String caption;
    @NonNull Photos content;

    public Optional<String> getCaption() {
        return Optional.ofNullable(caption);
    }
}
