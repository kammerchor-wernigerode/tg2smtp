package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link NotificationFactoryProvider} implementation that stores {@link NotificationFactory Notification factories}
 * in its encapsulated registry.
 *
 * @author Vincent Nadoll
 */
public class ConfigurableNotificationFactoryProvider
        implements NotificationFactoryProvider, NotificationFactoryRegistry {

    private final Map<Class<?>, NotificationFactory<?>> registry = new LinkedHashMap<>(8);

    @Override
    public <T> void addNotificationFactory(@NonNull Class<T> messageType, @NonNull NotificationFactory<T> factory) {
        registry.put(messageType, factory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<NotificationFactory<T>> findBy(@NonNull T message) {
        Class<?> messageType = message.getClass();
        return Optional.ofNullable(registry.get(messageType))
                .map(notificationFactory -> (NotificationFactory<T>) notificationFactory);
    }
}
