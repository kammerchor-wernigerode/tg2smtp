package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.audio.model.TitledAudio;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link AudioNotification}s from Telegram
 * {@link Audio} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class AudioNotificationFactory implements NotificationFactory<TitledAudio> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledAudio audio, @NonNull Metadata metadata) {
        AudioNotification notification = new AudioNotification(audio, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class AudioNotification extends MediaNotification {

        private final TitledAudio audio;
        private final Locale locale;

        public AudioNotification(TitledAudio audio, Downloader<MediaReference> downloader, Locale locale) {
            super(downloader);
            this.audio = audio;
            this.locale = locale;
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", audio.getCaption().orElse(null));

            return renderer.render("audio", locale, model);
        }

        @Override
        protected MediaReference create() {
            Audio content = audio.getContent();
            return new MediaReference(content.getFileId(), content.getFileName());
        }
    }
}
