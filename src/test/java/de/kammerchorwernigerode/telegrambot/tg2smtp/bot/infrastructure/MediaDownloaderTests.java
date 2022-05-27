package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class MediaDownloaderTests {

    private MediaDownloader downloader;

    private @Mock ThrowingFunction<GetFile, File, TelegramApiException> executor;
    private @Mock Function<File, String> pathExtractor;

    private MediaReference reference;

    @BeforeEach
    void setUp() {
        downloader = new MediaDownloader(executor, pathExtractor);

        reference = new MediaReference("foo");
    }

    @Test
    void initializeNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MediaDownloader(executor, null));
        assertThrows(IllegalArgumentException.class, () -> new MediaDownloader(null, pathExtractor));
    }

    @Test
    @SneakyThrows
    void failingExecution_shouldThrowException() {
        when(executor.apply(any())).thenThrow(TelegramApiException.class);

        assertThrows(IOException.class, () -> downloader.download(reference));
    }

    @Test
    @SneakyThrows
    void retrievingMalformedFileUrl_shouldThrowException() {
        File file = mock(File.class);
        when(executor.apply(any())).thenReturn(file);
        when(pathExtractor.apply(eq(file))).thenReturn("");

        assertThrows(IOException.class, () -> downloader.download(reference));
    }

    @Test
    @SneakyThrows
    void downloadingMedia_shouldSetResourcesFilename() {
        MediaReference reference = new MediaReference("foo", "foo.mp3");
        File file = mock(File.class);
        when(executor.apply(any())).thenReturn(file);
        when(pathExtractor.apply(eq(file))).thenReturn("https://example.com/foo.mp3");

        Resource resource = downloader.download(reference);

        assertEquals("foo.mp3", resource.getFilename());
    }
}
