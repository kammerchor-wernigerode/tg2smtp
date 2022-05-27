package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class LoggingNotificationServiceTests {

    @Mock
    private Renderer renderer;

    @Test
    void sendingNullNotification_shouldThrowException() {
        LoggingNotificationService service = new LoggingNotificationService(renderer);
        assertThrows(IllegalArgumentException.class, () -> service.send((Notification) null));
    }
}
