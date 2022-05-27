package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * VO-like that supplies a string message.
 *
 * @author Vincent Nadoll
 */
public interface Notification {

    default Optional<String> getSubject(@NonNull Renderer renderer) throws IOException {
        return Optional.empty();
    }

    String getMessage(@NonNull Renderer renderer) throws IOException;

    Stream<Resource> listAttachments();

    static Notification just(String message) {
        return just(renderer -> message);
    }

    static Notification just(@NonNull ThrowingFunction<Renderer, String, IOException> renderFunction) {
        return new Basic(renderFunction);
    }


    @RequiredArgsConstructor
    class Basic implements Notification {

        @NonNull
        private final ThrowingFunction<Renderer, String, IOException> renderFunction;

        @Override
        public String getMessage(@NonNull Renderer renderer) throws IOException {
            return renderFunction.apply(renderer);
        }

        @Override
        public Stream<Resource> listAttachments() {
            return null;
        }
    }
}
