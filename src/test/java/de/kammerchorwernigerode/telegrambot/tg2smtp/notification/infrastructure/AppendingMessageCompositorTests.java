package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        String composite = compositor.compose(singletonList(() -> "foo"));

        assertEquals("foo", composite);
    }

    @Test
    @SneakyThrows
    void composingMultiple_shouldAppendOrdered() {
        when(delimiter.toString()).thenReturn("\n-\n");

        String composite = compositor.compose(asList(() -> "foo", () -> "bar"));

        assertEquals(readFile("composite.txt"), composite);
    }

    public static String readFile(String filename) throws IOException {
        return IOUtils.toString(resourceStream(filename), StandardCharsets.UTF_8);
    }

    public static InputStream resourceStream(String filename) {
        return AppendingMessageCompositorTests.class.getClassLoader().getResourceAsStream(filename);
    }
}
