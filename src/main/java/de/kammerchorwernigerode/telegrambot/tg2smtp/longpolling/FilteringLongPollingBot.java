package de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.UpdateUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Vincent Nadoll
 */
@Slf4j
public class FilteringLongPollingBot extends LongPollingBotDecorator implements LongPollingBot {

    private final Predicate<Update> filter;
    private final AbsSender sender;

    public FilteringLongPollingBot(@NonNull LongPollingBot subject, @NonNull Predicate<Update> filter,
                                   @NonNull AbsSender sender) {
        super(subject);
        this.filter = filter;
        this.sender = sender;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        List<Update> filtered = updates.stream()
                .filter(update -> {
                    if (filter.test(update)) {
                        return true;
                    }

                    respondUnauthorized(update);
                    return false;
                }).collect(Collectors.toList());
        super.onUpdatesReceived(filtered);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (filter.test(update)) {
            super.onUpdateReceived(update);
        }

        respondUnauthorized(update);
    }

    @SneakyThrows
    private void respondUnauthorized(Update update) {
        if (log.isDebugEnabled()) log.debug("Chat not authorized");
        String text = "Your chat is not authorized to use this bot. "
                + "Please remove " + getBotUsername() + " or ask your bot administrator.";
        Optional<SendMessage> errorMessage = UpdateUtils.extractMessage(update)
                .map(Message::getChatId)
                .map(Object::toString)
                .map(chatId -> new SendMessage(chatId, text));
        if (errorMessage.isPresent()) sender.execute(errorMessage.get());
    }
}
