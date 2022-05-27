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
import org.telegram.telegrambots.meta.api.objects.Voice;

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
class VoiceNotificationFactoryTests {

    private VoiceNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;
    private @Mock Printer<Metadata> metadataPrinter;

    @BeforeEach
    void setUp() {
        factory = new VoiceNotificationFactory(configuration, downloader, metadataPrinter);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new VoiceNotificationFactory(null, downloader, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new VoiceNotificationFactory(configuration, null, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new VoiceNotificationFactory(configuration, downloader, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Voice voiceMessage = mock(Voice.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(voiceMessage, null));
    }

    @Test
    @SneakyThrows
    void creatingFromVoice_shouldDelegateDownload() {
        Voice voiceMessage = mock(Voice.class);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo");
        when(voiceMessage.getFileId()).thenReturn("foo");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(voiceMessage, createDefault());

        verify(downloader).download(mediaReference);
    }
}
