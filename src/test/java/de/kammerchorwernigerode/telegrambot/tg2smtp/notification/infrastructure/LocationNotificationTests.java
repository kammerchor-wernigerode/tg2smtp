package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.test.IntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.Locale;
import java.util.stream.Stream;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@SpringBootTest({"telegrambots.enabled=false"})
class LocationNotificationTests {

    @Autowired
    private Renderer renderer;

    @Mock
    private Location model;
    private LocationNotificationFactory factory;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        factory = new LocationNotificationFactory();
    }

    @ParameterizedTest
    @MethodSource("de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.Locales#configured")
    @SneakyThrows
    void renderingMessage_shouldSucceed(Locale locale) {
        Location model = new Location(42.0, 13.37, 0.0, 0, 0, 0);
        Notification notification = factory.create(model, createDefault(locale));

        String message = notification.getMessage(renderer);

        assertTrue(StringUtils.hasText(message));
    }

    @ParameterizedTest
    @MethodSource("de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.Locales#configured")
    @SneakyThrows
    void renderingSubject_shouldSucceed(Locale locale) {
        Notification notification = factory.create(model, createDefault(locale));

        String subject = notification.getSubject(renderer).get();

        assertTrue(StringUtils.hasText(subject));
    }

    @Test
    @SneakyThrows
    void listingAttachments_shouldReturnEmptyStream() {
        Notification notification = factory.create(model, createDefault());

        Stream<Resource> attachments = notification.listAttachments();

        assertEquals(0, attachments.count());
    }
}
