package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@link NotificationFactory} that creates templated
 * {@link MetadataHeadedNotificationDecorator metadata subject decorated} {@link PollNotification}s from Telegram
 * {@link Poll} messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class PollNotificationFactory implements NotificationFactory<Poll> {

    @Override
    public Notification create(@NonNull Poll message, @NonNull Metadata metadata) {
        PollNotification notification = new PollNotification(message, metadata.getLocale());
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }


    @RequiredArgsConstructor
    private static final class PollNotification implements Notification {

        private final Poll location;
        private final Locale locale;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            Map<String, Object> model = new HashMap<>();
            model.put("model", location);

            return renderer.render("poll", locale, model);
        }
    }
}
