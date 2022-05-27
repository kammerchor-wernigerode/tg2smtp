package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * VO-like that supplies a string message.
 *
 * @author Vincent Nadoll
 */
public interface Notification {

    @Deprecated
    String getMessage();

    default String getMessage(@NonNull Renderer renderer) throws IOException {
        return getMessage();
    }

    Stream<Resource> listAttachments();
}
