package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.location.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.location.app.LocationUrlResolver;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.net.URL;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class LocationPrinterTests {

    private @Mock LocationUrlResolver resolver;
    private @Mock Location location;

    private LocationPrinter printer;

    @BeforeEach
    void setUp() {
        printer = new LocationPrinter(resolver);
    }

    @Test
    void printingNullLocation_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(location, null));
    }

    @Test
    @SneakyThrows
    void printingLocation_shouldDelegateToResolver() {
        when(resolver.resolve(any(), any())).thenReturn(new URL("https://example.com/foo"));

        printer.print(location, Locale.getDefault());

        verify(resolver).resolve(any(), any());
    }

    @Test
    @SneakyThrows
    void printingLocation_shouldPrintResolved() {
        when(resolver.resolve(any(), any())).thenReturn(new URL("https://example.com/foo"));

        String string = printer.print(location, Locale.getDefault());

        assertEquals("https://example.com/foo", string);
    }
}
