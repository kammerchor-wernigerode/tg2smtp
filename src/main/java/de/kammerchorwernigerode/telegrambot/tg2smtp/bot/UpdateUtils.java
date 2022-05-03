package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * @author Vincent Nadoll
 */
@UtilityClass
public class UpdateUtils {

    public static Optional<Message> extractMessage(Update update) {
        return Optional.ofNullable(update.getMessage())
                .or(() -> Optional.ofNullable(update.getChannelPost()));
    }
}
