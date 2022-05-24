package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import lombok.NonNull;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

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
    public void addNotificationFactory(@NonNull NotificationFactory<?> factory) {
        Class<?> fieldType = getFieldType(factory, NotificationFactory.class);
        registry.put(fieldType, factory);
    }

    /**
     * Shamelessly stolen from
     * {@link org.springframework.format.support.FormattingConversionService#getFieldType(Object, Class)}
     */
    private static <T> Class<?> getFieldType(T instance, Class<T> genericInterface) {
        Class<?> fieldType = GenericTypeResolver.resolveTypeArgument(instance.getClass(), genericInterface);
        if (fieldType == null && instance instanceof DecoratingProxy) {
            fieldType = GenericTypeResolver.resolveTypeArgument(
                ((DecoratingProxy) instance).getDecoratedClass(), genericInterface);
        }
        Assert.notNull(fieldType, () -> "Unable to extract the parameterized field type from " +
            ClassUtils.getShortName(genericInterface) + " [" + instance.getClass().getName() +
            "]; does the class parameterize the <T> generic type?");
        return fieldType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<NotificationFactory<T>> findBy(@NonNull T message) {
        Class<?> messageType = message.getClass();
        return Optional.ofNullable(registry.get(messageType))
                .map(notificationFactory -> (NotificationFactory<T>) notificationFactory);
    }
}
