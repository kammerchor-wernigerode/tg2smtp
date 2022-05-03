package de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Nadoll
 */
class LongPollingBotDecoratorTests {

    private LongPollingBot subject;
    private LongPollingBotDecorator bot;

    @BeforeEach
    void setUp() {
        subject = mock(LongPollingBot.class);
        bot = new DefaultLongPollingBotDecorator(subject);
    }

    @Test
    void givenNullArgument_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultLongPollingBotDecorator(null));
    }

    @ParameterizedTest
    @MethodSource("methods")
    void executeMethod_shouldDelegateToSubject(Consumer<LongPollingBot> executor) {
        executor.accept(bot);

        executor.accept(verify(subject));
    }

    private static Arguments[] methods() {
        return new Arguments[]{
                arguments((Consumer<LongPollingBot>) LongPollingBot::getBotToken),
                arguments((Consumer<LongPollingBot>) LongPollingBot::getBotUsername),
                arguments((Consumer<LongPollingBot>) LongPollingBot::onRegister),
                arguments((Consumer<LongPollingBot>) LongPollingBot::getOptions),
                arguments(new Consumer<LongPollingBot>() {
                    @SneakyThrows
                    @Override
                    public void accept(LongPollingBot bot) {
                        bot.clearWebhook();
                    }
                }),
                arguments((Consumer<LongPollingBot>) LongPollingBot::onClosing),
                arguments((Consumer<LongPollingBot>) bot -> bot.onUpdateReceived(any())),
                arguments((Consumer<LongPollingBot>) bot -> bot.onUpdatesReceived(any())),
        };
    }


    private static final class DefaultLongPollingBotDecorator extends LongPollingBotDecorator {

        public DefaultLongPollingBotDecorator(LongPollingBot subject) {
            super(subject);
        }
    }
}
