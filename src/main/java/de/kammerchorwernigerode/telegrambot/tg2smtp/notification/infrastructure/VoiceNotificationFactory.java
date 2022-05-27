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
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link VoiceNotification}s from Telegram
 * {@link Voice} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class VoiceNotificationFactory implements NotificationFactory<Voice> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Voice voice, @NonNull Metadata metadata) {
        VoiceNotification notification = new VoiceNotification(voice, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class VoiceNotification extends MediaNotification {

        private final Voice voice;
        private final Locale locale;

        public VoiceNotification(Voice voice, Downloader<MediaReference> downloader, Locale locale) {
            super(downloader);
            this.voice = voice;
            this.locale = locale;
        }

        @Override
        protected MediaReference create() {
            return new MediaReference(voice.getFileId());
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderer.render("voice", locale, new Object());
        }
    }
}
