package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Video}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class VideoNotificationFactory implements NotificationFactory<Video> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Video video, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("video.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, msg -> "", "")
                .with(download(video));
    }

    @SneakyThrows
    private Resource download(Video video) {
        MediaReference mediaReference = new MediaReference(video.getFileId(), video.getFileName());
        return downloader.download(mediaReference);
    }
}
