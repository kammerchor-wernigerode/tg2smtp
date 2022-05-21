package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;

/**
 * Filter that tests {@link Notification}s if their {@link Notification#getMessage()} is empty or {@literal null}.
 *
 * @author Vincent Nadoll
 */
public class EmptyMessageFilter implements Predicate<Notification> {

    @Override
    public boolean test(@NonNull Notification notification) {
        String message = notification.getMessage();
        return StringUtils.hasText(message);
    }
}
