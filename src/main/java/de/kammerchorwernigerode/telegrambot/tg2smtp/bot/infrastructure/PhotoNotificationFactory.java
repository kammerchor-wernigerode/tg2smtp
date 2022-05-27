package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.app.PhotoPicker;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.TitledPhotos;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link PhotoNotification}s from Telegram
 * {@link PhotoSize} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class PhotoNotificationFactory implements NotificationFactory<TitledPhotos> {

    private final @NonNull PhotoPicker photoPicker;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledPhotos photos, @NonNull Metadata metadata) {
        Notification notification = new PhotoNotification(photos, photoPicker, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class PhotoNotification extends MediaNotification {

        private final TitledPhotos photos;
        private final PhotoPicker photoPicker;
        private final Locale locale;

        public PhotoNotification(TitledPhotos photos,
                                 PhotoPicker photoPicker,
                                 Downloader<MediaReference> downloader,
                                 Locale locale) {
            super(downloader);
            this.photos = photos;
            this.photoPicker = photoPicker;
            this.locale = locale;
        }

        @Override
        protected MediaReference create() {
            PhotoSize photo = photoPicker.pickFrom(photos.getContent());
            return new MediaReference(photo.getFileId());
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", photos.getCaption().orElse(null));

            return renderer.render("photo", locale, model);
        }
    }
}
