package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notifications.just;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class EmptyMessageFilterTests {

    private EmptyMessageFilter filter;

    @Mock
    private Renderer renderer;

    @BeforeEach
    void setUp() {
        filter = new EmptyMessageFilter(renderer);
    }

    @Test
    void initializingNullRenderer_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new EmptyMessageFilter(null));
    }

    @Test
    void testingNullNotification_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> filter.test(null));
    }

    @Test
    void testingNullContainingNotification_shouldNotSucceed() {
        boolean result = filter.test(just(null));

        assertFalse(result);
    }

    @Test
    void testingEmptyNotification_shouldNotSucceed() {
        boolean result = filter.test(just(""));

        assertFalse(result);
    }

    @Test
    void testingNonNullNotification_shouldSucceed() {
        boolean result = filter.test(just("foo"));

        assertTrue(result);
    }
}
