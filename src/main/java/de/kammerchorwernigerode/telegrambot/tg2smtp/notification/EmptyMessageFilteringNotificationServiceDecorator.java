package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * {@link NotificationService} that filters out empty messages of {@link Notification}s.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class EmptyMessageFilteringNotificationServiceDecorator implements NotificationService {

    @NonNull
    private final NotificationService subject;

    @Override
    public void send(@NonNull Notification notification) {
        if (hasMessage(notification)) {
            subject.send(notification);
        }
    }

    @Override
    public void send(@NonNull Collection<? extends Notification> notifications) {
        List<? extends Notification> filtered = notifications.stream()
                .filter(EmptyMessageFilteringNotificationServiceDecorator::hasMessage)
                .collect(toList());
        subject.send(filtered);
    }

    private static boolean hasMessage(Notification notification) {
        String message = notification.getMessage();
        return StringUtils.hasText(message);
    }
}
