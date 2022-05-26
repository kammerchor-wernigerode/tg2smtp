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
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

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
class StickerNotificationFactoryTests {

    private StickerNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new StickerNotificationFactory(configuration, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Sticker sticker = mock(Sticker.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(sticker, null));
    }

    @Test
    @SneakyThrows
    void creatingFromVoice_shouldDelegateDownload() {
        Sticker sticker = mock(Sticker.class);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo");
        when(sticker.getFileId()).thenReturn("foo");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(sticker, Locale.getDefault());

        verify(downloader).download(mediaReference);
    }
}