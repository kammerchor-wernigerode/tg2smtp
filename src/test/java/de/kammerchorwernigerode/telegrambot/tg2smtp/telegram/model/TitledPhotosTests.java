package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TitledPhotosTests {

    @Mock
    private Photos content;

    @Test
    void initializingNullCaption_shouldNotThrowException() {
        assertDoesNotThrow(() -> new TitledPhotos(null, content));
    }

    @Test
    void initializeNullContent_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TitledPhotos("", null));
    }

    @Test
    void givenNullCaption_shouldReturnEmptyOptional() {
        TitledPhotos titled = new TitledPhotos(null, content);

        Optional<String> caption = titled.getCaption();

        assertFalse(caption.isPresent());
    }

    @Test
    void givenNonNullCaption_shouldReturnTitle() {
        TitledPhotos titled = new TitledPhotos("foo", content);

        String caption = titled.getCaption().get();

        assertEquals("foo", caption);
    }

    @Test
    void givenContent_shouldReturnContent() {
        TitledPhotos titled = new TitledPhotos(null, this.content);

        Photos content = titled.getContent();

        assertEquals(this.content, content);
    }
}
