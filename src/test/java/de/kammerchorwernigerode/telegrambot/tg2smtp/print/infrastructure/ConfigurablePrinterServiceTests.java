package de.kammerchorwernigerode.telegrambot.tg2smtp.print.infrastructure;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.format.Printer;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class ConfigurablePrinterServiceTests {

    private static Locale locale;

    private ConfigurablePrinterService service;

    @Mock
    private StringPrinter printer;

    @BeforeAll
    static void beforeAll() {
        locale = Locale.getDefault();
    }

    @BeforeEach
    void setUp() {
        service = new ConfigurablePrinterService();
    }

    @Test
    void registeringNullPrinter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.addPrinter(null));
    }

    @Test
    void printingNullValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.print("null", null));
    }

    @Test
    void printingLocalized_shouldDelegate() {
        prepareRegistry("foo", locale);

        service.print("foo", locale);

        verify(printer).print(eq("foo"), eq(locale));
    }

    @Test
    void printingUnknown_shouldThrowException() {
        assertThrows(ConverterNotFoundException.class, () -> service.print("foo", locale));
    }

    private void prepareRegistry(String value, Locale locale) {
        service.addPrinter(printer);

        when(printer.print(eq(value), eq(locale))).thenReturn(value);
    }


    private static class StringPrinter implements Printer<String> {

        @Override
        public String print(String string, Locale locale) {
            return string;
        }
    }
}
