package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.MessageType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * {@link NotificationFactoryProvider} implementation that stores {@link NotificationFactory Notification factories}
 * in its encapsulated registry.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class ConfigurableNotificationFactoryProvider
        implements NotificationFactoryProvider, NotificationFactoryRegistry {

    @NonNull
    private final EnumSet<MessageType> active;
    private final Map<Class<?>, NotificationFactory<?>> registry = new LinkedHashMap<>(8);

    @Override
    public void addNotificationFactory(@NonNull NotificationFactory<?> factory) {
        Class<?> fieldType = getFieldType(factory, NotificationFactory.class);
        if (isActive(fieldType)) {
            registry.put(fieldType, factory);
        }
    }

    private boolean isActive(Class<?> fieldType) {
        return active.stream()
                .filter(byTelegramMessageType(isAssignableFrom(fieldType)).or(MessageType.ALL::equals))
                .map(MessageType::getTelegramType)
                .findAny().isPresent();
    }

    private static Predicate<MessageType> byTelegramMessageType(Predicate<Class<?>> testimonial) {
        return messageType -> testimonial.test(messageType.getTelegramType());
    }

    private static Predicate<Class<?>> isAssignableFrom(Class<?> type) {
        return self -> self.isAssignableFrom(type);
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
