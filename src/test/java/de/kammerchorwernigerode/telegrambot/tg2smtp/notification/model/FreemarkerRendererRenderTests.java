package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model;

import de.kammerchorwernigerode.telegrambot.tg2smtp.test.IntegrationTest;
import freemarker.template.TemplateNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.getDefault;
import static java.util.Locale.setDefault;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@SpringBootTest({"telegrambots.enabled=false"})
class FreemarkerRendererRenderTests {

    @Autowired
    private FreemarkerRenderer renderer;

    @BeforeAll
    static void beforeAll() {
        setDefault(ENGLISH);
    }

    @Test
    void renderingNullLocale_shouldNotThrowException() {
        assertDoesNotThrow(() -> renderer.render("test-plain.ftl", null, new Object()));
    }

    @Disabled("Powerful computers may run into issues with locales, because Spring reuses the same test bootstrapper for all Spring Boot integration tests")
    @Test
    @SneakyThrows
    void renderingNullLocale_shouldRenderDefaultLocale() {
        String render = renderer.render("test-plain.ftl", null, new Object());

        assertEquals("John Doe", render);
    }

    @Disabled("Powerful computers may run into issues with locales, because Spring reuses the same test bootstrapper for all Spring Boot integration tests")
    @Test
    @SneakyThrows
    void renderingUnsupportedLocale_shouldDefaultToFallbackLocale() {
        setDefault(FRENCH);
        String render = renderer.render("test-plain.ftl", null, new Object());

        assertEquals("John Doe", render);
        setDefault(ENGLISH);
    }

    @Test
    @SneakyThrows
    void renderingGermanLocale_shouldRenderGermanTemplate() {
        String render = renderer.render("test-plain.ftl", GERMAN, new Object());

        assertEquals("Max Mustermann", render);
    }

    @Test
    void renderingMissingTemplate_shouldThrowException() {
        assertThrows(TemplateNotFoundException.class, () -> renderer.render("foo.ftl", getDefault(), new Object()));
    }

    @Test
    void renderingNullModel_shouldNotThrowException() {
        assertDoesNotThrow(() -> renderer.render("test-plain.ftl", getDefault(), null));
    }

    @Test
    void renderingEmptyModel_shouldThrowException() {
        assertThrows(IOException.class, () -> renderer.render("test-parameterized.ftl", getDefault(), new Object()));
    }

    @Test
    void renderingNullModel_shouldThrowException() {
        assertThrows(IOException.class, () -> renderer.render("test-parameterized.ftl", getDefault(), null));
    }

    @Test
    @SneakyThrows
    void renderingParameterized_shouldRender() {
        Map<String, Object> model = Collections.singletonMap("name", "John");

        String render = renderer.render("test-parameterized.ftl", getDefault(), model);

        assertEquals("Name: John", render);
    }
}
