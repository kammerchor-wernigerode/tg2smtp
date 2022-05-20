package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class TextPrinterTests {

    private static Locale locale;

    private TextPrinter printer;

    @BeforeAll
    static void beforeAll() {
        locale = Locale.getDefault();
    }

    @BeforeEach
    void setUp() {
        printer = new TextPrinter();
    }

    @Test
    void printingNullPoll_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(null));
        assertThrows(IllegalArgumentException.class, () -> printer.print(null, locale));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print("text", null));
    }

    @Test
    @SneakyThrows
    void givenPoll_shouldPrint() {
        String text = "foo";

        String string = printer.print(text);

        assertEquals("foo", string);
    }
}
