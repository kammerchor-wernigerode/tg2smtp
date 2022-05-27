package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ListDecorator;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

/**
 * A strong typed {@link List} of {@link PhotoSize Telegram photos}.
 *
 * @author Vincent Nadoll
 */
public class Photos extends ListDecorator<PhotoSize> {

    public Photos(@NonNull List<PhotoSize> subject) {
        super(subject);
    }
}
