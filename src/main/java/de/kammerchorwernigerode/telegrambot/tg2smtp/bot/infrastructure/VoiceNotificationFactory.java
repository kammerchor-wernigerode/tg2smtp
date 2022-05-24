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
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.util.Locale;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.StaticPrinter.emptyString;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Voice}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class VoiceNotificationFactory implements NotificationFactory<Voice> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Voice voice, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("voice.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, emptyString(), "")
                .with(download(voice));
    }

    @SneakyThrows
    private Resource download(Voice voice) {
        MediaReference mediaReference = new MediaReference(voice.getFileId());
        return downloader.download(mediaReference);
    }
}
