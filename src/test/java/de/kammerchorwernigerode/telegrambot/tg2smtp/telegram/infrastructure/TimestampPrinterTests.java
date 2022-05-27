package de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.infrastructure;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimestampPrinterTests {

    private static DateTimeFormatter formatter;
    private static Instant timestamp;

    private TimestampPrinter printer;

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.GERMAN);
        formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        timestamp = now();
    }

    @BeforeEach
    void setUp() {
        printer = new TimestampPrinter(formatter);
    }

    @Test
    void initializingNullFormatter_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TimestampPrinter(null));
    }

    @Test
    void printingNullTimestamp_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(timestamp, null));
    }

    @Test
    void printingTimestamp_shouldFormat() {
        String print = printer.print(timestamp, Locale.getDefault());

        assertTrue(StringUtils.hasText(print));
    }
}
