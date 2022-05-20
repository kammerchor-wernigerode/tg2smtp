package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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
    @SneakyThrows
    void printingLocation_shouldDelegateToResolver() {
        when(resolver.resolve(eq(location))).thenReturn(new URL("https://example.com/foo"));

        printer.print(location);

        verify(resolver).resolve(eq(location));
    }

    @Test
    @SneakyThrows
    void printingLocation_shouldPrintResolved() {
        when(resolver.resolve(eq(location))).thenReturn(new URL("https://example.com/foo"));

        String string = printer.print(location);

        assertEquals("https://example.com/foo", string);
    }
}
