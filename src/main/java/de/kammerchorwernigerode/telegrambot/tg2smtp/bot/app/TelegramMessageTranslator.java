package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.Locale;
import java.util.Optional;

/**
 * Component that translates a Telegram {@link Message}'s content to a notification.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class TelegramMessageTranslator {

    private final @NonNull NotificationFactoryProvider notificationFactoryProvider;
    private final @NonNull LocaleResolver localeResolver;

    public Notification translate(@NonNull Message message) {
        Locale locale = localeResolver.resolve(message);

        if (message.hasText()) {
            String text = message.getText();
            return createNotification(text, locale);
        } else if (message.hasLocation()) {
            Location location = message.getLocation();
            return createNotification(location, locale);
        } else if (message.hasPoll()) {
            Poll poll = message.getPoll();
            return createNotification(poll, locale);
        } else {
            String sender = Optional.ofNullable(message.getFrom())
                    .map(User::getFirstName)
                    .orElse("Somebody");
            return () -> sender + " has sent a new message.";
        }
    }

    private <T> Notification createNotification(T message, Locale locale) {
        return notificationFactoryProvider.findBy(message)
                .map(factory -> factory.create(message, locale))
                .orElseThrow(() -> new IllegalArgumentException(""
                        + "Factory for message of type "
                        + message.getClass().getSimpleName()
                        + " could not be found"));
    }
}
