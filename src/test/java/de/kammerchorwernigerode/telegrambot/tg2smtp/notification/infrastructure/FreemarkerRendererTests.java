package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static java.util.Locale.getDefault;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreemarkerRendererTests {

    private @Mock Configuration configuration;
    private @Mock PrinterService printerService;

    private FreemarkerRenderer renderer;

    private @Mock Object model;

    @BeforeEach
    void setUp() {
        renderer = new FreemarkerRenderer(configuration, printerService);
    }

    @Test
    void initializingNullConfiguration_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerRenderer(null, printerService));
    }

    @Test
    void initializingNullPrinterService_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FreemarkerRenderer(configuration, null));
    }

    @Test
    void renderingNullTemplate_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, getDefault(), model));
    }

    @Test
    @SneakyThrows
    void renderingNullLocale_shouldNotThrowException() {
        Template template = mock(Template.class);
        when(configuration.getTemplate(eq("foo.ftl"), eq((Locale) null))).thenReturn(template);

        assertDoesNotThrow(() -> renderer.render("foo", null, model));
    }

    @Test
    @SneakyThrows
    void renderingNullModel_shouldNotThrowException() {
        Template template = mock(Template.class);
        when(configuration.getTemplate(any(String.class), any(Locale.class))).thenReturn(template);

        assertDoesNotThrow(() -> renderer.render("foo", getDefault(), null));
    }
}
