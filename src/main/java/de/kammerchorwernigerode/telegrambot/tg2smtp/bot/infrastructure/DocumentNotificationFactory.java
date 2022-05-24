package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledDocument;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Document}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class DocumentNotificationFactory implements NotificationFactory<TitledDocument> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledDocument document, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("document.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, msg -> "", document.getCaption().orElse(""))
                .with(download(document.getContent()));
    }

    @SneakyThrows
    private Resource download(Document document) {
        MediaReference mediaReference = new MediaReference(document.getFileId(), document.getFileName());
        return downloader.download(mediaReference);
    }
}
