package de.kammerchorwernigerode.telegrambot.tg2smtp.format.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class ConfigurablePrinterServiceTests {

    private static Locale locale;

    private ConfigurablePrinterService service;

    private @Mock Printer<String> printer;
    private Class<String> type;

    @BeforeAll
    static void beforeAll() {
        locale = Locale.getDefault();
    }

    @BeforeEach
    void setUp() {
        service = new ConfigurablePrinterService();

        type = String.class;
    }

    @Test
    void registeringNullClass_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.addPrinter(null, printer));
    }

    @Test
    void registeringNullPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.addPrinter(type, null));
    }

    @Test
    void printingNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.print(null));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.print(null, locale));
        assertThrows(IllegalArgumentException.class, () -> service.print("null", null));
    }

    @Test
    void printingString_shouldDelegate() {
        prepareRegistry("foo");

        service.print("foo");

        verify(printer).print(eq("foo"));
    }

    @Test
    void printingLocalized_shouldDelegate() {
        prepareRegistry("foo", locale);

        service.print("foo", locale);

        verify(printer).print(eq("foo"), eq(locale));
    }

    @Test
    void printingUnknown_shouldNotDelegate() {
        service.print("foo");

        verifyNoInteractions(printer);
    }

    @Test
    void printingUnknown_shouldReturnEmptyString() {
        String string = service.print("foo");

        assertEquals("", string);
    }

    @Test
    void printingLocalizedUnknown_shouldNotDelegate() {
        service.print("foo", locale);

        verifyNoInteractions(printer);
    }

    @Test
    void printingLocalizedUnknown_shouldReturnEmptyString() {
        String string = service.print("foo", locale);

        assertEquals("", string);
    }

    private void prepareRegistry(String value) {
        service.addPrinter(type, printer);

        when(printer.print(eq(value))).thenReturn(value);
    }

    private void prepareRegistry(String value, Locale locale) {
        service.addPrinter(type, printer);

        when(printer.print(eq(value), eq(locale))).thenReturn(value);
    }
}
