package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link StickerNotification}s from Telegram
 * {@link Sticker} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class StickerNotificationFactory implements NotificationFactory<Sticker> {

    private final @NonNull Downloader<MediaReference> downloader;

    @Override
    public Notification create(@NonNull Sticker sticker, @NonNull Metadata metadata) {
        Notification notification = new StickerNotification(sticker, downloader, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    private static final class StickerNotification extends MediaNotification {

        private final Sticker sticker;
        private final Locale locale;

        public StickerNotification(Sticker sticker,
                                   Downloader<MediaReference> downloader,
                                   Locale locale) {
            super(downloader);
            this.sticker = sticker;
            this.locale = locale;
        }

        @Override
        protected MediaReference create() {
            return new MediaReference(sticker.getFileId());
        }

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderer.render("sticker", locale, new Object());
        }
    }
}
