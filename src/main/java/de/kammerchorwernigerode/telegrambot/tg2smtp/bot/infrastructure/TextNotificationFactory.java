package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification.just;

/**
 * Trivial {@link String} {@link NotificationFactory} that returns its input message.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class TextNotificationFactory implements NotificationFactory<String> {

    @NonNull
    private final Printer<Metadata> metadataPrinter;

    @Override
    public Notification create(@NonNull String message, @NonNull Metadata metadata) {
        return new MetadataHeadedNotificationDecorator(metadata, metadataPrinter, just(message));
    }
}
