package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class VideoNoteNotificationFactoryTests {

    private VideoNoteNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new VideoNoteNotificationFactory(configuration, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new VideoNoteNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new VideoNoteNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        VideoNote videoNote = mock(VideoNote.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(videoNote, null));
    }

    @Test
    @SneakyThrows
    void creatingFromVoice_shouldDelegateDownload() {
        VideoNote videoNote = mock(VideoNote.class);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo");
        when(videoNote.getFileId()).thenReturn("foo");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(videoNote, Locale.getDefault());

        verify(downloader).download(mediaReference);
    }
}
