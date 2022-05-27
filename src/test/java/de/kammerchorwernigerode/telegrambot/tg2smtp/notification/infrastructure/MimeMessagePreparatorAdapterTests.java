package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class MimeMessagePreparatorAdapterTests {

    private MimeMessagePreparatorAdapter adapter;

    private @Mock Notification notification;
    private @Mock Renderer renderer;

    @BeforeEach
    void setUp() {
        adapter = new MimeMessagePreparatorAdapter(notification, renderer);
    }

    @Test
    void initializingNullNotification_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MimeMessagePreparatorAdapter(null, renderer));
    }

    @Test
    void initializingNullRenderer_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MimeMessagePreparatorAdapter(notification, null));
    }

    @Test
    void preparingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adapter.prepare(null));
    }

    @Test
    @SneakyThrows
    void preparingMessage_shouldSetText() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(notification.getMessage(renderer)).thenReturn("foo");

        adapter.prepare(mimeMessage);

        verify(mimeMessage).setText(eq("foo\n"), any(String.class));
    }

    @Test
    @SneakyThrows
    void preparingEmptyAttachments_shouldNotAddAttachments() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(notification.getMessage(renderer)).thenReturn("");

        adapter.prepare(mimeMessage);

        verify(mimeMessage).setText(eq("\n"), any(String.class));
        verifyNoMoreInteractions(mimeMessage);
    }
}
