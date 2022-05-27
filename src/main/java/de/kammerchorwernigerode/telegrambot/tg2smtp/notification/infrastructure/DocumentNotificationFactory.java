package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.document.model.TitledDocument;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link DocumentNotification}s from Telegram
 * {@link Document} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class DocumentNotificationFactory implements NotificationFactory<TitledDocument> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledDocument document, @NonNull Metadata metadata) {
        DocumentNotification notification = new DocumentNotification(document, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class DocumentNotification extends MediaNotification {

        private final TitledDocument document;
        private final Locale locale;

        public DocumentNotification(TitledDocument document, Downloader<MediaReference> downloader, Locale locale) {
            super(downloader);
            this.document = document;
            this.locale = locale;
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", document.getCaption().orElse(null));

            return renderer.render("document", locale, model);
        }

        @Override
        protected MediaReference create() {
            Document content = document.getContent();
            return new MediaReference(content.getFileId(), content.getFileName());
        }
    }
}
