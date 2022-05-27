package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class TextNotificationFactoryTests {

    private TextNotificationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TextNotificationFactory();
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("foo", null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Notification notification = factory.create("foo", createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
