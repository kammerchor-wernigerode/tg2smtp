package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link LocationNotification}s from Telegram
 * {@link Location} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class LocationNotificationFactory implements NotificationFactory<Location> {

    @Override
    public Notification create(@NonNull Location message, @NonNull Metadata metadata) {
        LocationNotification notification = new LocationNotification(message, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    @RequiredArgsConstructor
    private static final class LocationNotification implements Notification {

        private final Location location;
        private final Locale locale;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", location);

            return renderer.render("location.ftl", locale, model);
        }
    }
}
