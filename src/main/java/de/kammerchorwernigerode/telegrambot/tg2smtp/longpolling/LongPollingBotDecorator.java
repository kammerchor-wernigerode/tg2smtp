package de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

/**
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public abstract class LongPollingBotDecorator implements LongPollingBot {

    @Getter
    @NonNull
    @Delegate
    private final LongPollingBot subject;
}
