package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import lombok.NonNull;

import java.util.function.Predicate;

/**
 * {@link NotificationService} that removes {@link Notification}s not matching the given filter.
 *
 * @author Vincent Nadoll
 */
public class FilteringNotificationService implements NotificationService {

    @NonNull
    private final NotificationService subject;

    @NonNull
    private final Predicate<Notification> filter;

    public FilteringNotificationService(@NonNull NotificationService subject,
                                        @NonNull Predicate<Notification> filter) {
        this.subject = subject;
        this.filter = filter;
    }

    @Override
    public void send(@NonNull Notification notification) {
        if (filter.test(notification)) {
            subject.send(notification);
        }
    }
}
