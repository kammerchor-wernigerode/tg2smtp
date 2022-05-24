package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TitledAudioTests {

    @Mock
    private Audio content;

    @Test
    void initializingNullCaption_shouldNotThrowException() {
        assertDoesNotThrow(() -> new TitledAudio(null, content));
    }

    @Test
    void initializeNullContent_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TitledAudio("", null));
    }

    @Test
    void givenNullCaption_shouldReturnEmptyOptional() {
        TitledAudio titled = new TitledAudio(null, content);

        Optional<String> caption = titled.getCaption();

        assertFalse(caption.isPresent());
    }

    @Test
    void givenNonNullCaption_shouldReturnTitle() {
        TitledAudio titled = new TitledAudio("foo", content);

        String caption = titled.getCaption().get();

        assertEquals("foo", caption);
    }

    @Test
    void givenContent_shouldReturnContent() {
        TitledAudio titled = new TitledAudio(null, this.content);

        Audio content = titled.getContent();

        assertEquals(this.content, content);
    }
}
