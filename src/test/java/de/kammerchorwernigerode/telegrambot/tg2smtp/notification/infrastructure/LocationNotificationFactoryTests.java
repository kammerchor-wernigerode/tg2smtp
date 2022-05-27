package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Location;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
class LocationNotificationFactoryTests {

    private LocationNotificationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new LocationNotificationFactory();
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Location location = mock(Location.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(location, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Location location = mock(Location.class);

        Notification notification = factory.create(location, createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
