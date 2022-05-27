package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.function.Predicate;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.bot.support.UpdateUtils.extractMessage;

/**
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
class ChatIdAuthorizer implements Predicate<Update> {

    @NonNull
    private final Collection<Long> chatIds;

    @Override
    public boolean test(Update update) {
        return extractMessage(update)
                .map(Message::getChatId)
                .filter(chatIds::contains)
                .isPresent();
    }
}
