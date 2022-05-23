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
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Audio}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class AudioNotificationFactory implements NotificationFactory<Audio> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<Audio> downloader;

    @Override
    public Notification create(@NonNull Audio audio, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("audio.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, msg -> "", "")
                .with(download(audio));
    }

    @SneakyThrows
    private Resource download(Audio audio) {
        return downloader.download(audio);
    }
}
