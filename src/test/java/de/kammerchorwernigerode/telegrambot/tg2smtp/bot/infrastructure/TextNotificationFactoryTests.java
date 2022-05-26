package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void creatingNullMetadata_shouldNotThrowException() {
        assertDoesNotThrow(() -> factory.create("foo", null));
    }

    @Test
    void creatingMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo", createDefault());

        assertEquals("foo", notification.getMessage());
    }

    @Test
    void creatingLocalizedMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo", createDefault());

        assertEquals("foo", notification.getMessage());
    }
}
