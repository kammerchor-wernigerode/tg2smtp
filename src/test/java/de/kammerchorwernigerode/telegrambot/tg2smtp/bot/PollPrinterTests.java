package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
class PollPrinterTests {

    private static Locale locale;

    private PollPrinter printer;

    private Poll poll;
    private PollOption option1;
    private PollOption option2;

    @BeforeAll
    static void beforeAll() {
        locale = Locale.getDefault();
    }

    @BeforeEach
    void setUp() {
        printer = new PollPrinter();

        poll = mock(Poll.class);
        option1 = mock(PollOption.class);
        option2 = mock(PollOption.class);

        when(poll.getOptions()).thenAnswer(i -> List.of(option1, option2));
    }

    @Test
    void printingNullPoll_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(null, locale));
    }

    @Test
    void printingNullLocale_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> printer.print(poll, null));
    }

    @Test
    @SneakyThrows
    void givenPoll_shouldPrint() {
        when(poll.getQuestion()).thenReturn("Foo?");
        when(option1.getText()).thenReturn("Bar");
        when(option2.getText()).thenReturn("Baz");

        String string = printer.print(poll, Locale.getDefault());

        assertEquals(readFile("poll.txt"), string);
    }

    public static String readFile(String filename) throws IOException {
        return IOUtils.toString(resourceStream(filename), StandardCharsets.UTF_8);
    }

    public static InputStream resourceStream(String filename) {
        return PollPrinterTests.class.getClassLoader().getResourceAsStream(filename);
    }
}
