package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.Printer;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Metadatas.createDefault;
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
    private @Mock Printer<Metadata> metadataPrinter;

    @BeforeEach
    void setUp() {
        factory = new PollNotificationFactory(configuration, printer, metadataPrinter);
    }

    @Test
    void initializingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new PollNotificationFactory(null, printer, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new PollNotificationFactory(configuration, null, metadataPrinter));
        assertThrows(IllegalArgumentException.class, () -> new PollNotificationFactory(configuration, printer, null));
    }

    @Test
    void creatingNullMessage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, createDefault()));
    }

    @Test
    void creatingNullMetadata_shouldThrowException() {
        Poll poll = mock(Poll.class);

        assertThrows(IllegalArgumentException.class, () -> factory.create(poll, null));
    }

}
