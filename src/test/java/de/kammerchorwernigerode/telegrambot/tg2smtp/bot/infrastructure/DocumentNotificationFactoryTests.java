package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Document;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class DocumentNotificationFactoryTests {

    private DocumentNotificationFactory factory;

    private @Mock Downloader<MediaReference> downloader;

    @BeforeEach
    void setUp() {
        factory = new DocumentNotificationFactory(downloader);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new DocumentNotificationFactory(null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Document document = mock(Document.class);
        TitledDocument titledDocument = new TitledDocument("bar.pdf", document);

        assertThrows(IllegalArgumentException.class, () -> factory.create(titledDocument, null));
    }

    @Test
    void creatingNotification_shouldDecorate() {
        Document document = mock(Document.class);
        TitledDocument titledDocument = new TitledDocument("bar.pdf", document);

        Notification notification = factory.create(titledDocument, createDefault());

        assertEquals(MetadataHeadedNotificationDecorator.class, notification.getClass());
    }
}
