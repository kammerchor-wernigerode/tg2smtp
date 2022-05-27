package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
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
