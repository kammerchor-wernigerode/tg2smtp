package de.kammerchorwernigerode.telegrambot.tg2smtp.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;

/**
 * Defines callback methods to customize the Java-based configuration for this application.
 *
 * @author Vincent Nadoll
 */
public interface Configurer {

    default void addNotificationFactories(NotificationFactoryRegistry registry) {
    }
}
