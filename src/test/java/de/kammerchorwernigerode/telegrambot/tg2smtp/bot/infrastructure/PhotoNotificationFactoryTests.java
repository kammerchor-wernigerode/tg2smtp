package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
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
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
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
    private @Mock Printer<Metadata> metadataPrinter;

    @BeforeEach
    void setUp() {
        factory = new PhotoNotificationFactory(configuration, picker, downloader, metadataPrinter);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(null, picker, downloader, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(configuration, null, downloader, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(configuration, picker, null, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new PhotoNotificationFactory(configuration, picker, downloader, null));
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

        factory.create(titledPhotos, createDefault());

        verify(downloader).download(mediaReference);
    }
}
