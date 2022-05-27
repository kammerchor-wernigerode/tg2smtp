package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * Filter that tests {@link Notification}s if their {@link Notification#getMessage(Renderer)} is empty or
 * {@literal null}.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@RequiredArgsConstructor
public class EmptyMessageFilter implements Predicate<Notification> {

    @NonNull
    private final Renderer renderer;

    @Override
    public boolean test(@NonNull Notification notification) {
        try {
            String message = notification.getMessage(renderer);
            return StringUtils.hasText(message);
        } catch (IOException e) {
            if (log.isDebugEnabled()) log.debug("Renderer failed", e);
            return false;
        }
    }
}
