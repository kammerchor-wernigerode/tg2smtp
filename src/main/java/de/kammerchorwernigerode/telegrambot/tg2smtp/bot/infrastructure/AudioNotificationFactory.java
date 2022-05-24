package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.TitledAudio;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.Locale;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.StaticPrinter.emptyString;

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
    public Notification create(@NonNull TitledAudio audio, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("audio.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, emptyString(), audio.getCaption().orElse(null))
                .with(download(audio.getContent()));
    }

    @SneakyThrows
    private Resource download(Audio audio) {
        MediaReference mediaReference = new MediaReference(audio.getFileId(), audio.getFileName());
        return downloader.download(mediaReference);
    }
}
