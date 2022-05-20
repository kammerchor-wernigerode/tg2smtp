package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class NullSafePrinterServiceTests {

    private static Locale locale;

    @Mock
    private PrinterService subject;
    private PrinterService decorator;

    @BeforeAll
    static void beforeAll() {
        locale = Locale.getDefault();
    }

    @BeforeEach
    void setUp() {
        decorator = new NullSafePrinterService(subject);
    }

    @Test
    void printingObject_shouldDelegate() {
        decorator.print("foo");

        verify(subject).print("foo");
    }

    @Test
    void printingNull_shouldPrintEmptyString() {
        String string = decorator.print(null);

        assertEquals("", string);
    }

    @Test
    void printingNull_shouldNotDelegate() {
        decorator.print(null);

        verifyNoInteractions(subject);
    }

    @Test
    void printingLocalized_shouldDelegate() {
        decorator.print("foo", locale);

        verify(subject).print("foo", locale);
    }

    @Test
    void printingNullLocale_shouldNotDelegate() {
        decorator.print(null, locale);
        decorator.print("foo", null);
        decorator.print(null, null);

        verifyNoInteractions(subject);
    }
}
