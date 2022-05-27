package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.audio.model.TitledAudio;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.document.model.TitledDocument;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.photo.model.TitledPhotos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.video.model.TitledVideo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Optional;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Notifications.just;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TelegramMessageTranslatorTests {

    private TelegramMessageTranslator translator;

    private @Mock NotificationFactoryProvider provider;
    private @Mock Message message;

    @BeforeEach
    void setUp() {
        translator = new TelegramMessageTranslator(provider);
    }

    @Test
    void initializeNullProvider_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TelegramMessageTranslator(null));
    }

    @Test
    void translatingUnknown_shouldReturnEmptyOptional() {
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy(any())).thenReturn(Optional.empty());

        Optional<Notification> notification = translator.translate(message);

        assertFalse(notification.isPresent());
    }

    @Test
    void translatingUnregistered_shouldReturnEmptyOptional() {
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy("foo")).thenReturn(Optional.empty());

        Optional<Notification> notification = translator.translate(message);

        assertFalse(notification.isPresent());
    }

    @Test
    void translatingTextMessage_shouldReturnNotification() {
        Notification notification = just("foo");
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy("foo")).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingLocationMessage_shouldReturnNotification() {
        Location location = mock(Location.class);
        Notification notification = just("foo");
        when(message.hasLocation()).thenReturn(true);
        when(message.getLocation()).thenReturn(location);
        when(provider.findBy(location)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingPollMessage_shouldReturnNotification() {
        Poll poll = mock(Poll.class);
        Notification notification = just("foo");
        when(message.hasPoll()).thenReturn(true);
        when(message.getPoll()).thenReturn(poll);
        when(provider.findBy(poll)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingPhotoMessage_shouldReturnNotification() {
        Notification notification = just("foo");
        when(message.hasPhoto()).thenReturn(true);
        when(message.getCaption()).thenReturn("bar.jpg");
        when(message.getPhoto()).thenReturn(emptyList());
        when(provider.findBy(any(TitledPhotos.class))).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingDocumentMessage_shouldReturnNotification() {
        Document document = mock(Document.class);
        TitledDocument titledDocument = new TitledDocument("bar.pdf", document);
        Notification notification = just("foo");
        when(message.hasDocument()).thenReturn(true);
        when(message.getCaption()).thenReturn("bar.pdf");
        when(message.getDocument()).thenReturn(document);
        when(provider.findBy(titledDocument)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingAudioMessage_shouldReturnNotification() {
        Audio audio = mock(Audio.class);
        TitledAudio titledAudio = new TitledAudio("bar.mp3", audio);
        Notification notification = just("foo");
        when(message.hasAudio()).thenReturn(true);
        when(message.getCaption()).thenReturn("bar.mp3");
        when(message.getAudio()).thenReturn(audio);
        when(provider.findBy(titledAudio)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingVoiceMessage_shouldReturnNotification() {
        Voice voice = mock(Voice.class);
        Notification notification = just("foo");
        when(message.hasVoice()).thenReturn(true);
        when(message.getVoice()).thenReturn(voice);
        when(provider.findBy(voice)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingVideoMessage_shouldReturnNotification() {
        Video video = mock(Video.class);
        TitledVideo titledVideo = new TitledVideo("bar.mp4", video);
        Notification notification = just("foo");
        when(message.hasVideo()).thenReturn(true);
        when(message.getCaption()).thenReturn("bar.mp4");
        when(message.getVideo()).thenReturn(video);
        when(provider.findBy(titledVideo)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingVideoNote_shouldReturnNotification() {
        VideoNote videoNote = mock(VideoNote.class);
        Notification notification = just("foo");
        when(message.hasVideoNote()).thenReturn(true);
        when(message.getVideoNote()).thenReturn(videoNote);
        when(provider.findBy(videoNote)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingSticker_shouldReturnNotification() {
        Sticker sticker = mock(Sticker.class);
        Notification notification = just("foo");
        when(message.hasSticker()).thenReturn(true);
        when(message.getSticker()).thenReturn(sticker);
        when(provider.findBy(sticker)).thenReturn(Optional.of((msg, metadata) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }
}
