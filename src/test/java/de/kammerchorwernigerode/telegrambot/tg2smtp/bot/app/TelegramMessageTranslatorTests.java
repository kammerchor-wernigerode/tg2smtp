package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void initializeNullArgument_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TelegramMessageTranslator(null));
    }

    @Test
    void translatingUnknown_shouldThrowException() {
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> translator.translate(message));
    }

    @Test
    void translatingTextMessage_shouldReturnNotification() {
        Notification notification = () -> "foo";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("foo");
        when(provider.findBy("foo")).thenReturn(Optional.of((msg, locale) -> notification));

        Notification result = translator.translate(message);

        assertEquals(notification, result);
    }
}
