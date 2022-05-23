package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link PhotoSize}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class PhotoNotificationFactory implements NotificationFactory<Photos> {

    private final @NonNull Configuration configuration;
    private final @NonNull PhotoPicker photoPicker;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Photos photos, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("photo.ftl").locale(locale);
        PhotoSize photo = photoPicker.pickFrom(photos);

        return new FreemarkerNotification<>(template, configuration, object -> "", "")
                .with(download(photo));
    }

    @SneakyThrows
    private Resource download(PhotoSize photo) {
        MediaReference mediaReference = new MediaReference(photo.getFileId());
        return downloader.download(mediaReference);
    }
}
