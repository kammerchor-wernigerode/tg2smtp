package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

import java.util.stream.Stream;

@UtilityClass
public class Notifications {

    public static Notification just(String message) {
        return new Basic(message);
    }


    @RequiredArgsConstructor
    class Basic implements Notification {

        private final String message;

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public Stream<Resource> listAttachments() {
            return Stream.empty();
        }
    }
}
