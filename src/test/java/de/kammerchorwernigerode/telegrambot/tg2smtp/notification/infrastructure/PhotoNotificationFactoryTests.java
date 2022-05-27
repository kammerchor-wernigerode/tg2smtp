package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.TitledPhotos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class PhotoNotificationFactoryTests {

    private PhotoNotificationFactory factory;

    private @Mock PhotoPicker picker;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new PhotoNotificationFactory(picker, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(picker, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Photos photos = mock(Photos.class);
        TitledPhotos titledPhotos = new TitledPhotos("bar.jpg", photos);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledPhotos, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        PhotoSize photo = mock(PhotoSize.class);
        Photos photos = new Photos(singletonList(photo));
        TitledPhotos titledPhotos = new TitledPhotos("bar.jpg", photos);

        Notification notification = factory.create(titledPhotos, createDefault());

        assertTrue(notification instanceof MetadataHeadedNotificationDecorator);
    }
}
