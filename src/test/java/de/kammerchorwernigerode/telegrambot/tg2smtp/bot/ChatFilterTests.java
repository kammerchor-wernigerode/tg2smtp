package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
class ChatFilterTests {

    private ChatFilter filter;

    private Update update;

    @BeforeEach
    void setUp() {
        filter = new ChatFilter(Set.of(42L, 1337L));
        update = mock(Update.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Test
    void testingNonEligibleUpdate_shouldFailTest() {
        boolean result = filter.test(update);

        assertFalse(result);
    }

    @Test
    void testingChannelUpdateNonMatchingId_shouldFailTest() {
        when(update.getMessage()).thenReturn(null);
        when(update.getChannelPost().getChatId()).thenReturn(1L);

        boolean result = filter.test(update);

        assertFalse(result);
    }

    @Test
    void testingChannelUpdateMatchingId_shouldSucceedTest() {
        when(update.getMessage()).thenReturn(null);
        when(update.getChannelPost().getChatId()).thenReturn(42L);

        boolean result = filter.test(update);

        assertTrue(result);
    }

    @Test
    void testingChatUpdateNonMatchingId_shouldFailTest() {
        when(update.getChannelPost()).thenReturn(null);
        when(update.getMessage().getChatId()).thenReturn(1L);

        boolean result = filter.test(update);

        assertFalse(result);
    }

    @Test
    void testingChatUpdateMatchingId_shouldSucceedTest() {
        when(update.getChannelPost()).thenReturn(null);
        when(update.getMessage().getChatId()).thenReturn(42L);

        boolean result = filter.test(update);

        assertTrue(result);
    }

    @Test
    void testingMultipleUpdatesMatchingId_shouldSucceedTest() {
        Update update1 = mock(Update.class, Answers.RETURNS_DEEP_STUBS);

        when(update1.getMessage()).thenReturn(null);
        when(update1.getChannelPost().getChatId()).thenReturn(1337L);
        when(update.getChannelPost()).thenReturn(null);
        when(update.getMessage().getChatId()).thenReturn(42L);

        boolean result = filter.test(update);
        boolean result1 = filter.test(update1);

        assertTrue(result);
        assertTrue(result1);
    }
}
