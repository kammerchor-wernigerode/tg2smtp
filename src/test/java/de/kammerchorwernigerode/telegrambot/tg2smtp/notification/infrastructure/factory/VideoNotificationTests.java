package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.factory;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.video.model.TitledVideo;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Locale;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas.createDefault;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@SpringBootTest({"telegrambots.enabled=false"})
class VideoNotificationTests {

    private @Autowired Renderer renderer;
    private @MockBean Downloader<MediaReference> downloader;

    @Mock
    private Video model;
    private VideoNotificationFactory factory;
    private TitledVideo titled;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        factory = new VideoNotificationFactory(downloader);
        titled = new TitledVideo("foo", model);

        when(downloader.download(any())).thenReturn(new ClassPathResource("poll.txt"));
    }

    @ParameterizedTest
    @MethodSource("de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.factory.Locales#configured")
    @SneakyThrows
    void renderingMessage_shouldSucceed(Locale locale) {
        Notification notification = factory.create(titled, createDefault(locale));

        String message = notification.getMessage(renderer);

        assertTrue(StringUtils.hasText(message));
    }

    @ParameterizedTest
    @MethodSource("de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.factory.Locales#configured")
    @SneakyThrows
    void renderingSubject_shouldSucceed(Locale locale) {
        Notification notification = factory.create(titled, createDefault(locale));

        String subject = notification.getSubject(renderer).get();

        assertTrue(StringUtils.hasText(subject));
    }

    @Test
    @SneakyThrows
    void listingAttachments_shouldDownloadFile() {
        when(model.getFileId()).thenReturn("foo");
        when(model.getFileName()).thenReturn("bar.pdf");
        Notification notification = factory.create(titled, createDefault());

        notification.listAttachments();

        verify(downloader).download(any());
    }
}
