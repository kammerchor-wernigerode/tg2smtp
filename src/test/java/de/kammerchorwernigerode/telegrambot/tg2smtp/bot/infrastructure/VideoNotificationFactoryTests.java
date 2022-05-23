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
import org.telegram.telegrambots.meta.api.objects.Video;

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
class VideoNotificationFactoryTests {

    private VideoNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new VideoNotificationFactory(configuration, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new VideoNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new VideoNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Video video = mock(Video.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(video, null));
    }

    @Test
    @SneakyThrows
    void creatingFromVideo_shouldDelegateDownload() {
        Video video = mock(Video.class);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo", "bar.mp4");
        when(video.getFileId()).thenReturn("foo");
        when(video.getFileName()).thenReturn("bar.mp4");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(video);

        verify(downloader).download(mediaReference);
    }
}
