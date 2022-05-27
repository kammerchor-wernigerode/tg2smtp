package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.factory;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

import static java.util.Collections.singletonMap;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link TextNotification}s from Telegram
 * {@link String text} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class TextNotificationFactory implements NotificationFactory<String> {

    @Override
    public Notification create(@NonNull String message, @NonNull Metadata metadata) {
        TextNotification notification = new TextNotification(message, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    @RequiredArgsConstructor
    private static final class TextNotification implements Notification {

        private final String text;
        private final Locale locale;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderer.render("text", locale, singletonMap("text", text));
        }
    }
}
