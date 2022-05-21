package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
    void sendingNullNotifications_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> delegate.send((Collection<? extends Notification>) null));
    }

    @Test
    void sendingNonEligible_shouldNotDelegate() {
        when(filter.test(any())).thenReturn(false);

        delegate.send(() -> null);

        verify(subject, never()).send(any(Notification.class));
    }

    @Test
    void sendingMultipleNonEligible_shouldDelegateEmptyCollection() {
        when(filter.test(any())).thenReturn(false);

        delegate.send(Set.of(() -> null, () -> null));

        verify(subject).send(eq(new ArrayList<>()));
    }

    @Test
    void sendingEligible_shouldDelegate() {
        when(filter.test(any())).thenReturn(true);

        delegate.send(() -> "foo");

        verify(subject).send(any(Notification.class));
    }

    @Test
    void sendingMultipleEligible_shouldDelegate() {
        when(filter.test(any())).thenReturn(true);

        delegate.send(Set.of(() -> "foo", () -> "bar"));

        verify(subject).send(anyList());
    }

    @Test
    void sendingPartiallyNonEmptyMessages_shouldDelegateFiltered() {
        Notification notification = () -> "foo";
        when(filter.test(any())).thenReturn(false);
        when(filter.test(eq(notification))).thenReturn(true);

        delegate.send(Arrays.asList(notification, () -> null));

        verify(subject).send(eq(Collections.singletonList(notification)));
    }
}
