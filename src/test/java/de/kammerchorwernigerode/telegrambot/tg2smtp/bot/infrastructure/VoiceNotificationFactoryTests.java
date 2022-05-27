package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Voice;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class VoiceNotificationFactoryTests {

    private VoiceNotificationFactory factory;

    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new VoiceNotificationFactory(downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new VoiceNotificationFactory(null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Voice voiceMessage = mock(Voice.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(voiceMessage, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Voice voiceMessage = mock(Voice.class);

        Notification notification = factory.create(voiceMessage, createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
