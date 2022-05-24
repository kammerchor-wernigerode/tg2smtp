package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class ConfigurableNotificationFactoryProviderTests {

    private ConfigurableNotificationFactoryProvider provider;

    @BeforeEach
    void setUp() {
        provider = new ConfigurableNotificationFactoryProvider();
    }

    @Test
    void addingNullFactory_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.addNotificationFactory(null));
    }

    @Test
    void findingByNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.findBy(null));
    }

    @Test
    void findingUnregistered_shouldReturnEmpty() {
        Optional<NotificationFactory<Object>> factory = provider.findBy(new Object());

        assertFalse(factory.isPresent());
    }

    @Test
    void findingRegistered_shouldReturnFactory() {
        NotificationFactory<Object> factory = new TestNotificationFactory();
        provider.addNotificationFactory(factory);

        NotificationFactory<Object> found = provider.findBy(new Object()).get();

        assertEquals(factory, found);
    }


    private static final class TestNotificationFactory implements NotificationFactory<Object> {

        @Override
        public Notification create(@NonNull Object message, @NonNull Locale locale) {
            return () -> "";
        }
    }
}
