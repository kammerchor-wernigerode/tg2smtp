package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TextNotificationFactoryTests {

    private TextNotificationFactory factory;

    @Mock
    private Renderer renderer;

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
    @SneakyThrows
    void creatingMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo", createDefault());

        assertEquals("foo", notification.getMessage(renderer));
    }

    @Test
    @SneakyThrows
    void creatingLocalizedMessage_shouldReturnMessage() {
        Notification notification = factory.create("foo", createDefault());

        assertEquals("foo", notification.getMessage(renderer));
    }

    @Test
    @SneakyThrows
    void creatingMessage_shouldNotUseRenderer() {
        Notification notification = factory.create("foo", createDefault());

        notification.getMessage(renderer);

        verifyNoInteractions(renderer);
    }
}
