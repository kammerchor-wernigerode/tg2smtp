package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import org.springframework.core.io.Resource;

import java.util.stream.Stream;

/**
 * VO-like that supplies a string message.
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface Notification {

    String getMessage();

    default Stream<Resource> listAttachments() {
        return Stream.empty();
    }
}
