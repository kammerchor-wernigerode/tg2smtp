package de.kammerchorwernigerode.telegrambot.tg2smtp.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.ConfigurableNotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import org.springframework.context.annotation.Bean;

/**
 * Configuration main class for the application's Java config.
 *
 * @author Vincent Nadoll
 */
public class ConfigurationSupport {

    @Bean
    public NotificationFactoryProvider notificationFactoryProvider(Tg2SmtpMessageTypeProperties properties) {
        ConfigurableNotificationFactoryProvider factoryProvider =
                new ConfigurableNotificationFactoryProvider(properties.getActive());
        addNotificationFactories(factoryProvider);
        return factoryProvider;
    }

    protected void addNotificationFactories(NotificationFactoryRegistry registry) {
    }
}
