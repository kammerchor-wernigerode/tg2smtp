package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.Tg2SmtpBotProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class AudioDownloaderTests {

    private AudioDownloader downloader;

    private @Mock(answer = RETURNS_DEEP_STUBS) Tg2SmtpBotProperties properties;
    private @Mock ThrowingFunction<GetFile, File, TelegramApiException> executor;
    private @Mock Audio audio;

    @BeforeEach
    void setUp() {
        downloader = new AudioDownloader(properties, executor);
    }

    @Test
    void initializeNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AudioDownloader(null, executor));
        assertThrows(IllegalArgumentException.class, () -> new AudioDownloader(properties, null));
    }

    @Test
    @SneakyThrows
    void failingExecution_shouldThrowException() {
        when(audio.getFileId()).thenReturn("foo");
        when(executor.apply(any())).thenThrow(TelegramApiException.class);

        assertThrows(IOException.class, () -> downloader.download(audio));
    }

    @Test
    @SneakyThrows
    void retrievingMalformedFileUrl_shouldThrowException() {
        File file = mock(File.class);
        when(audio.getFileId()).thenReturn("foo");
        when(properties.getBot().getToken()).thenReturn("foo");
        when(executor.apply(any())).thenReturn(file);
        when(file.getFileUrl(any())).thenReturn("");

        assertThrows(IOException.class, () -> downloader.download(audio));
    }

    @Test
    @SneakyThrows
    void downloadingMedia_shouldSetResourcesFilename() {
        File file = mock(File.class);
        when(audio.getFileId()).thenReturn("foo");
        when(executor.apply(any())).thenReturn(file);
        when(file.getFileUrl(any())).thenReturn("https://example.com/foo.mp3");
        when(audio.getFileName()).thenReturn("foo.mp3");

        Resource resource = downloader.download(audio);

        assertEquals("foo.mp3", resource.getFilename());
    }
}
