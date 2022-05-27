package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledAudio;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Audio}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class AudioNotificationFactory implements NotificationFactory<TitledAudio> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull TitledAudio audio, @NonNull Metadata metadata) {
        TemplateBuilder template = new TemplateBuilder("audio.ftl").locale(metadata.getLocale());

        FreemarkerNotification notification = new FreemarkerNotification(template, configuration)
                .with(download(audio.getContent()))
                .with("model", audio.getCaption().orElse(null));
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }

    @SneakyThrows
    private Resource download(Audio audio) {
        MediaReference mediaReference = new MediaReference(audio.getFileId(), audio.getFileName());
        return downloader.download(mediaReference);
    }
}
