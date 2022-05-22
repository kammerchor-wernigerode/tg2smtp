package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app;

import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TemplateBuilderTests {

    private TemplateBuilder builder;

    @Mock
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        builder = new TemplateBuilder("foo");
    }

    @Test
    void initializingNullName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TemplateBuilder(null));
    }

    @Test
    void configuringNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> builder.locale(null));
    }

    @Test
    void configuringNullEncoding_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> builder.encoding(null));
    }

    @Test
    void buildingNullConfiguration_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> builder.build(null));
    }

    @Test
    @SneakyThrows
    void buildingDefault_shouldDefaultToUtf8() {
        builder.build(configuration);

        verify(configuration).getTemplate(any(), any(), eq(StandardCharsets.UTF_8.name()));
    }

    @Test
    @SneakyThrows
    void buildingDefault_shouldDelegate() {
        builder.build(configuration);

        verify(configuration).getTemplate(eq("foo"), eq(null), any());
    }

    @Test
    @SneakyThrows
    void buildingWithConfiguredLocale_shouldDelegate() {
        builder.locale(Locale.GERMANY);

        builder.build(configuration);

        verify(configuration).getTemplate(any(), eq(Locale.GERMANY), any());
    }

    @Test
    @SneakyThrows
    void buildingWithConfiguredEncoding_shouldDelegate() {
        builder.encoding(StandardCharsets.US_ASCII);

        builder.build(configuration);

        verify(configuration).getTemplate(any(), any(), eq(StandardCharsets.US_ASCII.name()));
    }
}
