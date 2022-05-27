package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Document}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class DocumentNotificationFactory implements NotificationFactory<TitledDocument> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledDocument document, @NonNull Metadata metadata) {
        TemplateBuilder template = new TemplateBuilder("document.ftl").locale(metadata.getLocale());

        FreemarkerNotification notification = new FreemarkerNotification(template, configuration)
                .with(download(document.getContent()))
                .with("model", document.getCaption().orElse(null));
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }

    @SneakyThrows
    private Resource download(Document document) {
        MediaReference mediaReference = new MediaReference(document.getFileId(), document.getFileName());
        return downloader.download(mediaReference);
    }
}
