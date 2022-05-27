package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

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
        DocumentNotification notification = new DocumentNotification(document, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    @RequiredArgsConstructor
    private final class DocumentNotification implements Notification {

        private final TitledDocument document;
        private final Locale locale;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", document.getCaption().orElse(null));

            return renderer.render("document.ftl", locale, model);
        }

        @Override
        public Stream<Resource> listAttachments() {
            return Stream.of(download(document.getContent()));
        }

        @SneakyThrows
        private Resource download(Document document) {
            MediaReference mediaReference = new MediaReference(document.getFileId(), document.getFileName());
            return downloader.download(mediaReference);
        }
    }
}
