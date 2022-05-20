package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;

import java.util.Collection;

/**
 * Abstraction of a notifier.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationService {

    void send(@NonNull Notification notification);

    default void send(@NonNull Collection<? extends Notification> notifications) {
        notifications.forEach(this::send);
    }
}
