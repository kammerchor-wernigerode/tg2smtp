package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataHeadedNotificationDecoratorTests {

    private @Mock Notification subject;
    private @Mock Renderer renderer;
    private @Mock User user;

    private Notification delegate;
    private Metadata metadata;


    @BeforeEach
    void setUp() {
        metadata = new Metadata(0, user);
        delegate = new MetadataHeadedNotificationDecorator(metadata, subject);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MetadataHeadedNotificationDecorator(null, subject));
        assertThrows(IllegalArgumentException.class, () -> new MetadataHeadedNotificationDecorator(metadata, null));
    }

    @Test
    @SneakyThrows
    void gettingSubject_shouldNotDelegate() {
        when(renderer.render(any(), any(), any())).thenReturn("foo");

        delegate.getSubject(renderer);

        verify(subject, never()).getSubject(any(Renderer.class));
    }

    @Test
    @SneakyThrows
    void gettingMessage_shouldDelegate() {
        delegate.getMessage(renderer);

        verify(subject).getMessage(any(Renderer.class));
    }

    @Test
    @SneakyThrows
    void listingAttachments_shouldDelegate() {
        delegate.listAttachments();

        verify(subject).listAttachments();
    }
}
