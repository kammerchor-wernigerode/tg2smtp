package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.Document;

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
class DocumentNotificationFactoryTests {

    private DocumentNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new DocumentNotificationFactory(configuration, downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new DocumentNotificationFactory(null, downloader));
        assertThrows(IllegalArgumentException.class, () -> new DocumentNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Document document = mock(Document.class);
        TitledDocument titledDocument = new TitledDocument("bar.pdf", document);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledDocument, null));
    }

    @Test
    @SneakyThrows
    void creatingFromDocument_shouldDelegateDownload() {
        Document document = mock(Document.class);
        TitledDocument titledDocument = new TitledDocument("bar.pdf", document);
        Resource attachment = mock(Resource.class);
        MediaReference mediaReference = new MediaReference("foo", "bar.pdf");
        when(document.getFileId()).thenReturn("foo");
        when(document.getFileName()).thenReturn("bar.pdf");
        when(downloader.download(eq(mediaReference))).thenReturn(attachment);

        factory.create(titledDocument);

        verify(downloader).download(mediaReference);
    }
}
