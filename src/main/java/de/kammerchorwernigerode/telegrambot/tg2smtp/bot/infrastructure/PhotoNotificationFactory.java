package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledPhotos;
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
public class PhotoNotificationFactory implements NotificationFactory<TitledPhotos> {

    private final @NonNull Configuration configuration;
    private final @NonNull PhotoPicker photoPicker;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledPhotos photos, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("photo.ftl").locale(locale);
        PhotoSize photo = photoPicker.pickFrom(photos.getContent());

        return new FreemarkerNotification<>(template, configuration, (model, lang) -> "", photos.getCaption().orElse(null))
                .with(download(photo));
    }

    @SneakyThrows
    private Resource download(PhotoSize photo) {
        MediaReference mediaReference = new MediaReference(photo.getFileId());
        return downloader.download(mediaReference);
    }
}
