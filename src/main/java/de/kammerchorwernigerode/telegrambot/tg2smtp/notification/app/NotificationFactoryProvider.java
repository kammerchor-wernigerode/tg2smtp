package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import lombok.NonNull;

import java.util.Optional;

/**
 * Provides {@link de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification}s based on their message type.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationFactoryProvider {

    <T> Optional<NotificationFactory<T>> findBy(@NonNull T message);
}
