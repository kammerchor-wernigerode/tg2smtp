package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class LoggingNotificationServiceTests {

    @Test
    void sendingNullNotification_shouldThrowException() {
        LoggingNotificationService service = new LoggingNotificationService();
        assertThrows(IllegalArgumentException.class, () -> service.send((Notification) null));
    }
}
