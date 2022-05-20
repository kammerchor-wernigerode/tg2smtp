package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class EmptyMessageFilteringNotificationServiceDecoratorTests {

    @Mock
    private NotificationService subject;
    private NotificationService delegate;

    @BeforeEach
    void setUp() {
        delegate = new EmptyMessageFilteringNotificationServiceDecorator(subject);
    }

    @Test
    void initializingNullSubject_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new EmptyMessageFilteringNotificationServiceDecorator(null));
    }

    @Test
    void sendingNullNotification_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> delegate.send((Notification) null));
    }

    @Test
    void sendingNullNotifications_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> delegate.send((Collection<? extends Notification>) null));
    }

    @Test
    void sendingNullContainingMessageNotification_shouldNotDelegate() {
        delegate.send(() -> null);

        verify(subject, never()).send(any(Notification.class));
    }

    @Test
    void sendingEmptyMessageNotification_shouldNotDelegate() {
        delegate.send(() -> "");

        verify(subject, never()).send(any(Notification.class));
    }

    @Test
    void sendingNullContainingMessageNotifications_shouldDelegateEmptyCollection() {
        delegate.send(Set.of(() -> null, () -> null));

        verify(subject).send(eq(new ArrayList<>()));
    }

    @Test
    void sendingEmptyMessageNotifications_shouldDelegateEmptyCollection() {
        delegate.send(Set.of(() -> "", () -> ""));

        verify(subject).send(eq(new ArrayList<>()));
    }

    @Test
    void sendingMessage_shouldDelegate() {
        delegate.send(() -> "foo");

        verify(subject).send(any(Notification.class));
    }

    @Test
    void sendingMessages_shouldDelegate() {
        delegate.send(Set.of(() -> "foo", () -> "bar"));

        verify(subject).send(anyList());
    }

    @Test
    void sendingPartiallyNonEmptyMessages_shouldDelegateFiltered() {
        Notification notification = () -> "foo";
        delegate.send(Set.of(notification, () -> null));

        verify(subject).send(eq(Collections.singletonList(notification)));
    }
}
