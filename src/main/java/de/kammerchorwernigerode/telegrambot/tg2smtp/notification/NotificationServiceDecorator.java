package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Abstract decorator that delegates method calls to the given subject.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public abstract class NotificationServiceDecorator implements NotificationService {

    @Getter
    @NonNull
    @Delegate
    private final NotificationService subject;
}
