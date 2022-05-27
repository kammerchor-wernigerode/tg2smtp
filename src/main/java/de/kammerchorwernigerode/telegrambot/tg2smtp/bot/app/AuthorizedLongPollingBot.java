package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.UpdateUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
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
public class AuthorizedLongPollingBot implements LongPollingBot {

    @NonNull
    @Delegate(excludes = Overrides.class)
    private final LongPollingBot subject;
    private final Predicate<Update> authorizer;
    private final AbsSender sender;

    public AuthorizedLongPollingBot(@NonNull LongPollingBot subject, @NonNull Predicate<Update> authorizer,
                                    @NonNull AbsSender sender) {
        this.subject = subject;
        this.authorizer = authorizer;
        this.sender = sender;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        List<Update> filtered = updates.stream()
                .filter(update -> {
                    if (authorizer.test(update)) {
                        return true;
                    }

                    respondUnauthorized(update);
                    return false;
                }).collect(Collectors.toList());
        subject.onUpdatesReceived(filtered);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (authorizer.test(update)) {
            subject.onUpdateReceived(update);
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


    private interface Overrides {

        void onUpdatesReceived(List<Update> updates);

        void onUpdateReceived(Update update);
    }
}
