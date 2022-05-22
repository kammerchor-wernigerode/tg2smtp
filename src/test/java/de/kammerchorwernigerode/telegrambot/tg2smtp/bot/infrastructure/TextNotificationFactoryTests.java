package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

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
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldNotThrowException() {
        assertDoesNotThrow(() -> factory.create("foo", null));
    }

    @Test
    void creatingMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo");

        assertEquals("foo", notification.getMessage());
    }

    @Test
    void creatingLocalizedMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo", Locale.getDefault());

        assertEquals("foo", notification.getMessage());
    }
}
