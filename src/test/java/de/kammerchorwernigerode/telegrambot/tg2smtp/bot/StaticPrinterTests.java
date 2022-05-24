package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.StaticPrinter.emptyString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Vincent Nadoll
 */
class StaticPrinterTests {

    @Test
    void printingNullObject_shouldNotThrowException() {
        assertDoesNotThrow(() -> emptyString().print(null, Locale.getDefault()));
    }

    @Test
    void printingNullLocale_shouldNotThrowException() {
        assertDoesNotThrow(() -> emptyString().print(new Object(), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", ""})
    void givingAnyString_shouldAlwaysPrintEmpty(String input) {
        emptyString().print(input, null);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 42})
    void givingAnyString_shouldAlwaysPrintEmpty(int input) {
        emptyString().print(input, null);
    }
}
