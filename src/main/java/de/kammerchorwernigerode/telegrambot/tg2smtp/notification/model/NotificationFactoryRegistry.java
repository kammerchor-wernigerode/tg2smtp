package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.NonNull;

/**
 * SPI-component that registers {@link NotificationFactory Notification factories}.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface NotificationFactoryRegistry {

    void addNotificationFactory(@NonNull NotificationFactory<?> factory);
}
