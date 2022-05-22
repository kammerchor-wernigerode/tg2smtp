package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
@ExtendWith(MockitoExtension.class)
class PollNotificationFactoryTests {

    private PollNotificationFactory factory;

    private @Mock Configuration configuration;
    private @Mock PollPrinter printer;

    @BeforeEach
    void setUp() {
        factory = new PollNotificationFactory(configuration, printer);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new PollNotificationFactory(null, printer));
        assertThrows(IllegalArgumentException.class, () -> new PollNotificationFactory(configuration, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Locale.getDefault()));
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
    }

    @Test
    void creatingNullLocale_shouldThrowException() {
        Poll poll = mock(Poll.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(poll, null));
    }

}
