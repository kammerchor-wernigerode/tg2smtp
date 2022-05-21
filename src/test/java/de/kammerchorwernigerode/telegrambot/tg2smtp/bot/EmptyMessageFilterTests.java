package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class EmptyMessageFilterTests {

    private EmptyMessageFilter filter;

    @BeforeEach
    void setUp() {
        filter = new EmptyMessageFilter();
    }

    @Test
    void testingNullNotification_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> filter.test(null));
    }

    @Test
    void testingNullContainingNotification_shouldNotSucceed() {
        boolean result = filter.test(() -> null);

        assertFalse(result);
    }

    @Test
    void testingEmptyNotification_shouldNotSucceed() {
        boolean result = filter.test(() -> "");

        assertFalse(result);
    }

    @Test
    void testingNonNullNotification_shouldSucceed() {
        boolean result = filter.test(() -> "foo");

        assertTrue(result);
    }
}
