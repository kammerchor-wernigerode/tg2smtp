package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.VideoNote;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link VideoNoteNotification}s from Telegram
 * {@link VideoNote video notes}.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class VideoNoteNotificationFactory implements NotificationFactory<VideoNote> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull VideoNote video, @NonNull Metadata metadata) {
        VideoNoteNotification notification = new VideoNoteNotification(video, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class VideoNoteNotification extends MediaNotification {

        private final VideoNote video;
        private final Locale locale;

        public VideoNoteNotification(VideoNote video, Downloader<MediaReference> downloader, Locale locale) {
            super(downloader);
            this.video = video;
            this.locale = locale;
        }

        @Override
        protected MediaReference create() {
            return new MediaReference(video.getFileId());
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderer.render("video-note", locale, new Object());
        }
    }
}
