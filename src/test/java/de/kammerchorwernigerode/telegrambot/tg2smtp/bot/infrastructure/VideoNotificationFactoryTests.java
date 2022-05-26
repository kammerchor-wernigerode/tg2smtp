package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledVideo;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.Video;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
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
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Video video = mock(Video.class);
        TitledVideo titledVideo = new TitledVideo("bar.mp4", video);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledVideo, null));
    }

    @Test
    @SneakyThrows
    void creatingFromVideo_shouldDelegateDownload() {
        Video video = mock(Video.class);
        TitledVideo titledVideo = new TitledVideo("bar.mp4", video);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo", "bar.mp4");
        when(video.getFileId()).thenReturn("foo");
        when(video.getFileName()).thenReturn("bar.mp4");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(titledVideo, createDefault());

        verify(downloader).download(mediaReference);
    }
}
