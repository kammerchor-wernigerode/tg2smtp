package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

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

    public Notification translate(@NonNull Message message) {
        if (message.hasText()) {
            String text = message.getText();
            return createNotification(text);
        } else {
            String sender = Optional.ofNullable(message.getFrom())
                    .map(User::getFirstName)
                    .orElse("Somebody");
            return () -> sender + " has sent a new message.";
        }
    }

    private <T> Notification createNotification(T message) {
        return notificationFactoryProvider.findBy(message)
                .map(factory -> factory.create(message))
                .orElseThrow(() -> new IllegalArgumentException(""
                        + "Factory for message of type "
                        + message.getClass().getSimpleName()
                        + " could not be found"));
    }
}
