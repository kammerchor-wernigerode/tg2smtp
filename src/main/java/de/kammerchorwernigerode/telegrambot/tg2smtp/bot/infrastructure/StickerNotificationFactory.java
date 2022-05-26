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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Locale;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.common.Printers.emptyString;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Sticker}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class StickerNotificationFactory implements NotificationFactory<Sticker> {

    private final @NonNull Configuration configuration;
    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Sticker sticker, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("sticker.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, emptyString(), "")
                .with(download(sticker));
    }

    @SneakyThrows
    private Resource download(Sticker sticker) {
        MediaReference mediaReference = new MediaReference(sticker.getFileId());
        return downloader.download(mediaReference);
    }
}
