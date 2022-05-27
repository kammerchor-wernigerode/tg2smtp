package de.kammerchorwernigerode.telegrambot.tg2smtp.notification;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataHeadedNotificationTests {

    private MetadataHeadedNotification notification;

    private @Mock Printer<Metadata> printer;
    private @Mock Renderer renderer;
    private @Mock User user;

    private Metadata metadata;

    @BeforeEach
    void setUp() {
        metadata = new Metadata(0, user);

        notification = new MetadataHeadedNotification(metadata, printer) {
            @Override
            public String getMessage(@NonNull Renderer renderer) throws IOException {
                return null;
            }
        };
    }

    @Test
    void initializingNullMetadata_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MetadataHeadedNotification(null, printer) {
            @Override
            public String getMessage(@NonNull Renderer renderer) throws IOException {
                return null;
            }
        });
    }

    @Test
    void initializingNullPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MetadataHeadedNotification(metadata, null) {
            @Override
            public String getMessage(@NonNull Renderer renderer) throws IOException {
                return null;
            }
        });
    }

    @Test
    @SneakyThrows
    void gettingSubject_shouldPrint() {
        when(renderer.render(any(), any(), any())).thenReturn("foo");

        String subject = notification.getSubject(renderer).get();

        assertEquals("foo", subject);
    }
}
