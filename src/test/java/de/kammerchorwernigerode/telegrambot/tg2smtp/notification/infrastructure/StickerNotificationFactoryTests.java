package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class StickerNotificationFactoryTests {

    private StickerNotificationFactory factory;

    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new StickerNotificationFactory(downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Sticker sticker = mock(Sticker.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(sticker, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Sticker sticker = mock(Sticker.class);

        Notification notification = factory.create(sticker, createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
