package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Photos;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
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
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.Optional;

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
    private @Mock LocaleResolver localeResolver;
    private @Mock Message message;

    @BeforeEach
    void setUp() {
        translator = new TelegramMessageTranslator(provider, localeResolver);
    }

    @Test
    void initializeNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TelegramMessageTranslator(null, localeResolver));
        assertThrows(IllegalArgumentException.class, () -> new TelegramMessageTranslator(provider, null));
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
        Notification notification = () -> "foo";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy("foo")).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingLocationMessage_shouldReturnNotification() {
        Location location = mock(Location.class);
        Notification notification = () -> "foo";
        when(message.hasLocation()).thenReturn(true);
        when(message.getLocation()).thenReturn(location);
        when(provider.findBy(location)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingPollMessage_shouldReturnNotification() {
        Poll poll = mock(Poll.class);
        Notification notification = () -> "foo";
        when(message.hasPoll()).thenReturn(true);
        when(message.getPoll()).thenReturn(poll);
        when(provider.findBy(poll)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingPhotoMessage_shouldReturnNotification() {
        Notification notification = () -> "foo";
        when(message.hasPhoto()).thenReturn(true);
        when(message.getPhoto()).thenReturn(emptyList());
        when(provider.findBy(any(Photos.class))).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingDocumentMessage_shouldReturnNotification() {
        Document document = mock(Document.class);
        Notification notification = () -> "foo";
        when(message.hasDocument()).thenReturn(true);
        when(message.getDocument()).thenReturn(document);
        when(provider.findBy(document)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingAudioMessage_shouldReturnNotification() {
        Audio audio = mock(Audio.class);
        Notification notification = () -> "foo";
        when(message.hasAudio()).thenReturn(true);
        when(message.getAudio()).thenReturn(audio);
        when(provider.findBy(audio)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingVoiceMessage_shouldReturnNotification() {
        Voice voice = mock(Voice.class);
        Notification notification = () -> "foo";
        when(message.hasVoice()).thenReturn(true);
        when(message.getVoice()).thenReturn(voice);
        when(provider.findBy(voice)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }

    @Test
    void translatingVideoMessage_shouldReturnNotification() {
        Video video = mock(Video.class);
        Notification notification = () -> "foo";
        when(message.hasVideo()).thenReturn(true);
        when(message.getVideo()).thenReturn(video);
        when(provider.findBy(video)).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message).get();

        assertEquals(notification, result);
    }
}
