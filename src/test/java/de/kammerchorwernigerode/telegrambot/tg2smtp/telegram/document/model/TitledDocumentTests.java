package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.document.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class TitledDocumentTests {

    @Mock
    private Document content;

    @Test
    void initializingNullCaption_shouldNotThrowException() {
        assertDoesNotThrow(() -> new TitledDocument(null, content));
    }

    @Test
    void initializeNullContent_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TitledDocument("", null));
    }

    @Test
    void givenNullCaption_shouldReturnEmptyOptional() {
        TitledDocument titled = new TitledDocument(null, content);

        Optional<String> caption = titled.getCaption();

        assertFalse(caption.isPresent());
    }

    @Test
    void givenNonNullCaption_shouldReturnTitle() {
        TitledDocument titled = new TitledDocument("foo", content);

        String caption = titled.getCaption().get();

        assertEquals("foo", caption);
    }

    @Test
    void givenContent_shouldReturnContent() {
        TitledDocument titled = new TitledDocument(null, this.content);

        Document content = titled.getContent();

        assertEquals(this.content, content);
    }
}
