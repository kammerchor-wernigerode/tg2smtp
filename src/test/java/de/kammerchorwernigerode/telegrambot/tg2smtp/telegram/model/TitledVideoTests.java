package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TitledVideoTests {

    @Mock
    private Video content;

    @Test
    void initializingNullCaption_shouldNotThrowException() {
        assertDoesNotThrow(() -> new TitledVideo(null, content));
    }

    @Test
    void initializeNullContent_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TitledVideo("", null));
    }

    @Test
    void givenNullCaption_shouldReturnEmptyOptional() {
        TitledVideo titled = new TitledVideo(null, content);

        Optional<String> caption = titled.getCaption();

        assertFalse(caption.isPresent());
    }

    @Test
    void givenNonNullCaption_shouldReturnTitle() {
        TitledVideo titled = new TitledVideo("foo", content);

        String caption = titled.getCaption().get();

        assertEquals("foo", caption);
    }

    @Test
    void givenContent_shouldReturnContent() {
        TitledVideo titled = new TitledVideo(null, this.content);

        Video content = titled.getContent();

        assertEquals(this.content, content);
    }
}
