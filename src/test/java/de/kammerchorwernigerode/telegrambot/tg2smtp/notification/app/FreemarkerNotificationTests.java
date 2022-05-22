package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class FreemarkerNotificationTests {

    private static final String MODEL = "foo";

    private FreemarkerNotification<String> notification;

    private @Mock TemplateBuilder templateBuilder;
    private @Mock Configuration configuration;
    private @Mock Printer<String> printer;

    @BeforeEach
    void setUp() {
        notification = new FreemarkerNotification<>(templateBuilder, configuration, printer, MODEL);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification<>(null, configuration, printer, MODEL));
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification<>(templateBuilder, null, printer, MODEL));
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification<>(templateBuilder, configuration, null, MODEL));
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerNotification<>(templateBuilder, configuration, printer, null));
    }

    @Test
    @SneakyThrows
    void failingTemplateBuild_shouldThrowException() {
        when(templateBuilder.build(configuration)).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> notification.getMessage());
    }
}
