package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notifications.just;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class FilteringNotificationServiceTests {

    @Mock
    private NotificationService subject;
    private NotificationService delegate;

    @Mock
    private Predicate<Notification> filter;

    @BeforeEach
    void setUp() {
        delegate = new FilteringNotificationService(subject, filter);
    }

    @Test
    void initializingNullSubject_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FilteringNotificationService(null, filter));
    }

    @Test
    void sendingNullNotification_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> delegate.send((Notification) null));
    }

    @Test
    void sendingNonEligible_shouldNotDelegate() {
        when(filter.test(any())).thenReturn(false);

        delegate.send(just(null));

        verify(subject, never()).send(any(Notification.class));
    }

    @Test
    void sendingEligible_shouldDelegate() {
        when(filter.test(any())).thenReturn(true);

        delegate.send(just("foo"));

        verify(subject).send(any(Notification.class));
    }
}
