package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
class PollNotificationFactoryTests {

    private PollNotificationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PollNotificationFactory();
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Poll poll = mock(Poll.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(poll, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Poll poll = mock(Poll.class);

        Notification notification = factory.create(poll, createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
