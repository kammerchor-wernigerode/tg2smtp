package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadata;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.model.Metadatas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataPrinterTests {

    private MetadataPrinter printer;

    private @Mock Printer<User> authorPrinter;
    private @Mock Printer<Instant> timestampPrinter;

    @BeforeEach
    void setUp() {
        printer = new MetadataPrinter(authorPrinter, timestampPrinter);
    }

    @Test
    void initializingNullAuthorPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MetadataPrinter(null, timestampPrinter));
    }

    @Test
    void initializingNullTimestampPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MetadataPrinter(authorPrinter, null));
    }

    @Test
    void printingNullMetadata_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(Metadatas.createDefault(), null));
    }

    @Test
    void printingMetadata_shouldNotUseAuthorsLanguage() {
        User author = mock(User.class);
        Metadata metadata = new Metadata(0, author);

        printer.print(metadata, Locale.getDefault());

        verify(author, never()).getLanguageCode();
    }

    @Test
    void printingAuthor_shouldUseLocaleParameter() {
        User author = mock(User.class);
        Metadata metadata = new Metadata(0, author);
        Locale locale = Locale.ENGLISH;

        printer.print(metadata, locale);

        verify(authorPrinter).print(eq(author), eq(locale));
    }

    @Test
    void printingTimestamp_shouldUseLocaleParameter() {
        User author = mock(User.class);
        Metadata metadata = new Metadata(0, author);
        Locale locale = Locale.ENGLISH;

        printer.print(metadata, locale);

        verify(timestampPrinter).print(any(Instant.class), eq(locale));
    }

    @Test
    void printingMetadata_shouldFormatMetadata() {
        User author = mock(User.class);
        Metadata metadata = new Metadata(0, author);
        when(authorPrinter.print(eq(author), any())).thenReturn("foo");
        when(timestampPrinter.print(any(Instant.class), any())).thenReturn("bar");

        String print = printer.print(metadata, Locale.getDefault());

        assertEquals("foo — bar", print);
    }
}
