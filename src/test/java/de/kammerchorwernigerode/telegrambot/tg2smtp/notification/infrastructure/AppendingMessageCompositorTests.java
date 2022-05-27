package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notifications.just;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppendingMessageCompositorTests {

    private AppendingMessageCompositor compositor;

    private @Mock CharSequence delimiter;
    private @Mock Renderer renderer;

    @BeforeEach
    void setUp() {
        compositor = new AppendingMessageCompositor(delimiter, renderer);
    }

    @Test
    void initializingNullDelimiter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AppendingMessageCompositor(null, renderer));
    }

    @Test
    void initializingNullRenderer_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AppendingMessageCompositor(delimiter, null));
    }

    @Test
    void composingNullList_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> compositor.compose(null));
    }

    @Test
    void composingEmptyList_shouldReturnEmptyString() {
        String composite = compositor.compose(emptyList());

        assertEquals("", composite);
    }

    @Test
    void composingSingleNotification_shouldReturnNotificationsMessage() {
        String composite = compositor.compose(singletonList(just("foo")));

        assertEquals("foo", composite);
    }

    @Test
    @SneakyThrows
    void composingMultiple_shouldAppendOrdered() {
        when(delimiter.toString()).thenReturn("\n-\n");

        String composite = compositor.compose(asList(just("foo"), just("bar")));

        assertEquals(readFile("composite.txt"), composite);
    }

    @Test
    @SneakyThrows
    void composingSingleHeaded_shouldReturnNotificationsMessage() {
        Notification notification = mockNotification("Foo - Bar", "Baz");

        String composite = compositor.compose(singletonList(notification));

        assertEquals("[Foo - Bar] Baz", composite);
    }

    @Test
    @SneakyThrows
    void composingMultipleHeaded_shouldAppendOrdered() {
        when(delimiter.toString()).thenReturn("\n");
        Notification notification1 = mockNotification("Foo - Bar", "Baz");
        Notification notification2 = mockNotification("Baz", "Bar");

        String composite = compositor.compose(asList(notification1, notification2));

        assertEquals("[Foo - Bar] Baz\n[Baz] Bar", composite);
    }

    private Notification mockNotification(String subject, String message) throws IOException {
        Notification notification = mock(Notification.class);
        when(notification.getSubject(any())).thenReturn(Optional.ofNullable(subject));
        when(notification.getMessage(any())).thenReturn(message);
        return notification;
    }

    public static String readFile(String filename) throws IOException {
        return IOUtils.toString(resourceStream(filename), StandardCharsets.UTF_8);
    }

    public static InputStream resourceStream(String filename) {
        return AppendingMessageCompositorTests.class.getClassLoader().getResourceAsStream(filename);
    }
}
