package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;

/**
 * Abstraction of a notifier.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationService {

    void send(@NonNull Notification notification);
}
