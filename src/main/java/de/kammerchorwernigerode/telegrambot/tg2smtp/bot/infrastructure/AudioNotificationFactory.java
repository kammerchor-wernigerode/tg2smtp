package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledAudio;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

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
        AudioNotification notification = new AudioNotification(audio, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    @RequiredArgsConstructor
    private final class AudioNotification implements Notification {

        private final TitledAudio audio;
        private final Locale locale;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", audio.getCaption().orElse(null));

            return renderer.render("audio.ftl", locale, model);
        }

        @Override
        public Stream<Resource> listAttachments() {
            return Stream.of(download(audio.getContent()));
        }

        @SneakyThrows
        private Resource download(Audio audio) {
            MediaReference mediaReference = new MediaReference(audio.getFileId(), audio.getFileName());
            return downloader.download(mediaReference);
        }
    }
}
