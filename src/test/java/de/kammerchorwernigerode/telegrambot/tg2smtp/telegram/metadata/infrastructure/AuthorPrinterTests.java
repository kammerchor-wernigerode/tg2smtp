package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.metadata.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorPrinterTests {

    private AuthorPrinter printer;

    private @Mock MessageSource messageSource;
    private @Mock User author;

    @BeforeEach
    void setUp() {
        printer = new AuthorPrinter(messageSource);
    }

    @Test
    void initializingNullMessageSource_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new AuthorPrinter(null));
    }

    @Test
    void printingNullAuthor_shouldNotThrowException() {
        assertDoesNotThrow(() -> printer.print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(author, null));
    }

    @Test
    void printingNullAuthor_shouldCallFallback() {
        printer.print(null, Locale.getDefault());

        verify(messageSource).getMessage(any(), eq(null), any(), any());
    }

    @Test
    void printingNullAuthor_shouldReturnFallback() {
        when(messageSource.getMessage(any(), eq(null), any(), any())).thenReturn("Moderator");

        String print = printer.print(null, Locale.getDefault());

        assertEquals("Moderator", print);
    }

    @Test
    void printingAuthor_shouldNotInteractWithMessageSource() {
        printer.print(author, Locale.getDefault());

        verifyNoInteractions(messageSource);
    }

    @Test
    void printingAuthor_shouldReturnFirstname() {
        when(author.getFirstName()).thenReturn("John");
        when(author.getLastName()).thenReturn(null);

        String print = printer.print(author, Locale.getDefault());

        assertEquals("John", print);
    }

    @Test
    void printingAuthor_shouldReturnFullName() {
        when(author.getFirstName()).thenReturn("John");
        when(author.getLastName()).thenReturn("Doe");

        String print = printer.print(author, Locale.getDefault());

        assertEquals("John Doe", print);
    }
}
