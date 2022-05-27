package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;

/**
 * Factory that creates new {@link Notification}s based on their type.
 *
 * @param <T> type of the {@link Notification}'s message source type
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationFactory<T> {

    Notification create(@NonNull T message, @NonNull Metadata metadata);
}
