package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.audio.model.TitledAudio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Audio;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class AudioNotificationFactoryTests {

    private AudioNotificationFactory factory;

    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new AudioNotificationFactory(downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AudioNotificationFactory(null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Audio audio = mock(Audio.class);
        TitledAudio titledAudio = new TitledAudio("foo.mp3", audio);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledAudio, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Audio audio = mock(Audio.class);
        TitledAudio titledAudio = new TitledAudio("bar.mp3", audio);

        Notification notification = factory.create(titledAudio, createDefault());

        assertEquals(MetadataHeadedNotificationDecorator.class, notification.getClass());
    }
}
