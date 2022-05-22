package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * Trivial {@link String} {@link NotificationFactory} that returns its input message.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class TextNotificationFactory implements NotificationFactory<String> {

    @Override
    public Notification create(@NonNull String message, @Nullable Locale locale) {
        return () -> message;
    }
}
