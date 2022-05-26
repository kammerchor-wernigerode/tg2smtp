package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Trivial {@link String} {@link NotificationFactory} that returns its input message.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class TextNotificationFactory implements NotificationFactory<String> {

    @Override
    public Notification create(@NonNull String message, @Nullable Metadata metadata) {
        return () -> message;
    }
}
