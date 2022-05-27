package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.factory;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.video.model.TitledVideo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.io.IOException;
import java.util.Locale;

import static java.util.Collections.singletonMap;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link VideoNotification}s from Telegram
 * {@link Video video} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class VideoNotificationFactory implements NotificationFactory<TitledVideo> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledVideo video, @NonNull Metadata metadata) {
        VideoNotification notification = new VideoNotification(video, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class VideoNotification extends MediaNotification {

        private final TitledVideo video;
        private final Locale locale;

        public VideoNotification(TitledVideo video, Downloader<MediaReference> downloader, Locale locale) {
            super(downloader);
            this.video = video;
            this.locale = locale;
        }

        @Override
        protected MediaReference create() {
            Video video = this.video.getContent();
            return new MediaReference(video.getFileId(), video.getFileName());
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderer.render("video", locale, singletonMap("model", video.getCaption().orElse(null)));
        }
    }
}
