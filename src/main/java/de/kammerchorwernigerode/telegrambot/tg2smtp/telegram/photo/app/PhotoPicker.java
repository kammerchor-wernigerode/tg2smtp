package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.app;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

/**
 * Function that reduces a {@link List} of {@link PhotoSize Telegram photos} to one.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface PhotoPicker {

    PhotoSize pickFrom(@NonNull List<PhotoSize> candidates) throws UndecidableException;


    /**
     * Exception that indicates that a picking-decision could not be made.
     *
     * @author Vincent Nadoll
     */
    final class UndecidableException extends RuntimeException {

        public UndecidableException(String message) {
            super(message);
        }
    }
}
