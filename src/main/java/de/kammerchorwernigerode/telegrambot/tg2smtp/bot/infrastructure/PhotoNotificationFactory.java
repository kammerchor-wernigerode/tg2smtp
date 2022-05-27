package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledPhotos;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link PhotoSize}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class PhotoNotificationFactory implements NotificationFactory<TitledPhotos> {

    private final @NonNull Configuration configuration;
    private final @NonNull PhotoPicker photoPicker;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledPhotos photos, @NonNull Metadata metadata) {
        TemplateBuilder template = new TemplateBuilder("photo.ftl").locale(metadata.getLocale());
        PhotoSize photo = photoPicker.pickFrom(photos.getContent());

        Notification notification = new FreemarkerNotification(template, configuration)
                .with(download(photo))
                .with("model", photos.getCaption().orElse(null));
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }

    @SneakyThrows
    private Resource download(PhotoSize photo) {
        MediaReference mediaReference = new MediaReference(photo.getFileId());
        return downloader.download(mediaReference);
    }
}
