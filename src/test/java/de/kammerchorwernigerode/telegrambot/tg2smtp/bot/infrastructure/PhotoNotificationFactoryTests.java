package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledPhotos;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class PhotoNotificationFactoryTests {

    private PhotoNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock PhotoPicker picker;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new PhotoNotificationFactory(configuration, picker, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(null, picker, downloader));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(configuration, null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(configuration, picker, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Photos photos = mock(Photos.class);
        TitledPhotos titledPhotos = new TitledPhotos("bar.jpg", photos);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledPhotos, null));
    }

    @Test
    @SneakyThrows
    void creatingFromPhotos_shouldDelegateDownload() {
        PhotoSize photo = mock(PhotoSize.class);
        Resource attachment = mock(Resource.class);
        Photos photos = new Photos(singletonList(photo));
        TitledPhotos titledPhotos = new TitledPhotos("bar.jpg", photos);
        MediaReference mediaReference = new MediaReference("foo");
        when(photo.getFileId()).thenReturn("foo");
        when(picker.pickFrom(eq(photos))).thenReturn(photo);
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(titledPhotos, Locale.getDefault());

        verify(downloader).download(mediaReference);
    }
}
