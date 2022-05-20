package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vincent Nadoll
 */
class ToStringPrinterTests {

    private ToStringPrinter printer;

    @BeforeEach
    void setUp() {
        printer = new ToStringPrinter();
    }

    @Test
    void printingNullValue_shouldReturnEmptyString() {
        String string = printer.print(null);

        assertEquals("", string);
    }

    @Test
    void printingBean_shouldReturnValue() {
        String string = printer.print(new Bean("foo"));

        assertEquals("foo", string);
    }

    @Test
    void printingLocalizedNullValue_shouldReturnEmptyString() {
        String string = printer.print(new Bean("foo"), null);

        assertEquals("foo", string);
    }

    @Test
    void printingNullLocale_shouldReturnEmptyString() {
        String string = printer.print(new Bean("foo"), Locale.getDefault());

        assertEquals("foo", string);
    }


    @Value
    private static class Bean {

        String value;

        @Override
        public String toString() {
            return value;
        }
    }
}
