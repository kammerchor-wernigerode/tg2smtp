package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

@UtilityClass
public class Notifications {

    public static Notification just(String message) {
        return new Basic(message);
    }


    @RequiredArgsConstructor
    class Basic implements Notification {

        private final String message;

        @Override
        public String getMessage(@Nullable Renderer renderer) {
            return message;
        }
    }
}
