package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * VO that encapsulates necessary Telegram media properties.
 *
 * @author Vincent Nadoll
 */
@Value
public class MediaReference {

    @NonNull String fileId;
    @Nullable String filename;

    public MediaReference(@NonNull String fileId) {
        this.fileId = fileId;
        this.filename = null;
    }

    public MediaReference(@NonNull String fileId, @Nullable String filename) {
        this.fileId = fileId;
        this.filename = filename;
    }

    public Optional<String> getFilename() {
        return Optional.ofNullable(filename);
    }
}
