package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.NonNull;

import java.util.Locale;

/**
 * Factory that creates new {@link Notification}s based on their type.
 *
 * @param <T> type of the {@link Notification}'s message source type
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationFactory<T> {

    Notification create(@NonNull T message, @NonNull Locale locale);
}
