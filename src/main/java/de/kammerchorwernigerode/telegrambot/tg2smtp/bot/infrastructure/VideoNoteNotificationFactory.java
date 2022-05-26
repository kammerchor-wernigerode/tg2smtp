package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.VideoNote;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram
 * {@link VideoNote video notes}.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class VideoNoteNotificationFactory implements NotificationFactory<VideoNote> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull VideoNote video, @NonNull Metadata metadata) {
        TemplateBuilder template = new TemplateBuilder("video-note.ftl").locale(metadata.getLocale());

        return new FreemarkerNotification(template, configuration)
                .with(download(video));
    }

    @SneakyThrows
    private Resource download(VideoNote video) {
        MediaReference mediaReference = new MediaReference(video.getFileId());
        return downloader.download(mediaReference);
    }
}
