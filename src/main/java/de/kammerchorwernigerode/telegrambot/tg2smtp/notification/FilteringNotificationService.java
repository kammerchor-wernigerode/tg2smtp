package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;

import java.util.function.Predicate;

/**
 * {@link NotificationService} that removes {@link Notification}s not matching the given filter.
 *
 * @author Vincent Nadoll
 */
public class FilteringNotificationService extends NotificationServiceDecorator
        implements NotificationService {

    @NonNull
    private final Predicate<Notification> filter;

    public FilteringNotificationService(@NonNull NotificationService subject,
                                        @NonNull Predicate<Notification> filter) {
        super(subject);
        this.filter = filter;
    }

    @Override
    public void send(@NonNull Notification notification) {
        if (filter.test(notification)) {
            super.send(notification);
        }
    }
}
