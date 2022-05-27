package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

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
class StickerNotificationFactoryTests {

    private StickerNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;
    private @Mock Printer<Metadata> metadataPrinter;

    @BeforeEach
    void setUp() {
        factory = new StickerNotificationFactory(configuration, downloader, metadataPrinter);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(null, downloader, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(configuration, null, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new StickerNotificationFactory(configuration, downloader, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
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

        factory.create(sticker, createDefault());

        verify(downloader).download(mediaReference);
    }
}
