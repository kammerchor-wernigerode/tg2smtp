package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * {@link NotificationService} that prints {@link Notification} to <em>stdout</em>.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@Service
@Profile("debug")
public class LoggingNotificationService implements NotificationService {

    @Override
    public void send(@NonNull Notification notification) {
        System.out.println(notification.getMessage());
    }
}
