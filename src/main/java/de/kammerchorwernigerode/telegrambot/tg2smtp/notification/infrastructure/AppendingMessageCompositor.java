package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MessageCompositor;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Component
public class AppendingMessageCompositor implements MessageCompositor {

    private final CharSequence delimiter;

    public AppendingMessageCompositor(@NonNull @Qualifier("notificationDelimiter") CharSequence delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String compose(@NonNull List<Notification> notifications) {
        return notifications.stream()
                .map(Notification::getMessage)
                .collect(joining(delimiter));
    }
}
