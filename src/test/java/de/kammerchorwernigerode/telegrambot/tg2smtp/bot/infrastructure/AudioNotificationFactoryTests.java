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
import org.telegram.telegrambots.meta.api.objects.Audio;

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
class AudioNotificationFactoryTests {

    private AudioNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new AudioNotificationFactory(configuration, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AudioNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new AudioNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Audio audio = mock(Audio.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(audio, null));
    }

    @Test
    @SneakyThrows
    void creatingFromAudio_shouldDelegateDownload() {
        Audio audio = mock(Audio.class);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo", "bar.mp3");
        when(audio.getFileId()).thenReturn("foo");
        when(audio.getFileName()).thenReturn("bar.mp3");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(audio);

        verify(downloader).download(mediaReference);
    }
}
