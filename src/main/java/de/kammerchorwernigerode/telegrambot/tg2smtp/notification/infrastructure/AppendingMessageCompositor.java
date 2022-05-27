package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MessageCompositor;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction.sneaky;
import static java.util.stream.Collectors.joining;

@Component
public class AppendingMessageCompositor implements MessageCompositor {

    private final CharSequence delimiter;
    private final Renderer renderer;

    public AppendingMessageCompositor(@NonNull @Qualifier("notificationDelimiter") CharSequence delimiter,
                                      @NonNull Renderer renderer) {
        this.delimiter = delimiter;
        this.renderer = renderer;
    }

    @Override
    public String compose(@NonNull List<Notification> notifications) {
        return notifications.stream()
                .map(sneaky(renderUsing(renderer)))
                .collect(joining(delimiter));
    }

    private static ThrowingFunction<Notification, String, IOException> renderUsing(Renderer renderer) {
        return notification -> notification.getMessage(renderer);
    }
}
