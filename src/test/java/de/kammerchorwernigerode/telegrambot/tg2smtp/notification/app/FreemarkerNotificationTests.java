package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class FreemarkerNotificationTests {

    private static final String MODEL = "foo";

    private FreemarkerNotification notification;

    private @Mock TemplateBuilder templateBuilder;
    private @Mock Configuration configuration;

    @BeforeEach
    void setUp() {
        notification = new FreemarkerNotification(templateBuilder, configuration);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification(null, configuration));
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification(templateBuilder, null));
    }

    @Test
    @SneakyThrows
    void failingTemplateBuild_shouldThrowException() {
        when(templateBuilder.build(configuration)).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> notification.getMessage());
    }

    @Test
    void addingNullAttachment_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> notification.with(null));
    }

    @Test
    void addingAttachment_shouldReturnSameNotificationInstance() {
        Resource attachment = mock(Resource.class);

        FreemarkerNotification result = notification.with(attachment);

        assertSame(notification, result);
    }

    @Test
    void listingNewNotification_shouldReturnEmptyStream() {
        Stream<Resource> attachments = notification.listAttachments();

        assertEquals(0, attachments.count());
    }

    @Test
    void addingAttachment_shouldReturnNonEmptyStream() {
        Resource attachment = mock(Resource.class);
        notification.with(attachment);

        Stream<Resource> attachments = notification.listAttachments();

        assertEquals(1, attachments.count());
    }

    @Test
    void addingAttachment_shouldContainAttachment() {
        Resource attachment = mock(Resource.class);
        notification.with(attachment);

        Stream<Resource> attachments = notification.listAttachments();

        assertEquals(attachment, attachments.findFirst().get());
    }

    @Test
    void addingNullDataKey_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> notification.with(null, MODEL));
    }

    @Test
    void addNullDataValue_shouldNotThrowException() {
        assertDoesNotThrow(() -> notification.with("model", null));
    }
}
