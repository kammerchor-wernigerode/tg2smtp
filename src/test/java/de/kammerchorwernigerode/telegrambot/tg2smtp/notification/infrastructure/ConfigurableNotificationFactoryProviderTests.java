package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Class<Object> messageType;
    private @Mock NotificationFactory<Object> factory;

    @BeforeEach
    void setUp() {
        messageType = Object.class;

        provider = new ConfigurableNotificationFactoryProvider();
    }

    @Test
    void addingNullType_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.addNotificationFactory(null, factory));
    }

    @Test
    void addingNullFactory_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> provider.addNotificationFactory(messageType, null));
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
        provider.addNotificationFactory(Object.class, factory);

        NotificationFactory<Object> found = provider.findBy(new Object()).get();

        assertEquals(factory, found);
    }
}
