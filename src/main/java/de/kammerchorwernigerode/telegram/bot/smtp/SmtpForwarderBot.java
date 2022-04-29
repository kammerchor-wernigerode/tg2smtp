package de.kammerchorwernigerode.telegram.bot.smtp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * This is more a playground than it is a working forwarder.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({SmtpForwarderBotProperties.class})
public class SmtpForwarderBot extends TelegramLongPollingBot {

    private final SmtpForwarderBotProperties properties;

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = extractMessage(update);
        if (null == message) {
            if (log.isDebugEnabled()) log.debug("Message is empty, skipping this update={}", update);
            return;
        }

        Long chatId = message.getChatId();
        if (log.isTraceEnabled()) log.trace("Received message from chat w/ chatId={}", chatId);
        if (!properties.getChatId().contains(chatId)) {
            if (log.isDebugEnabled()) log.debug("Chat not authorized");
            String text = "Your chat is not authorized to use this bot. "
                    + "Please remove " + properties.getUsername() + ".";
            SendMessage errorMessage = new SendMessage(chatId.toString(), text);
            execute(errorMessage);
            return;
        }

        SendMessage cc = new SendMessage(chatId.toString(), message.toString());
        execute(cc);
    }

    @Nullable
    private Message extractMessage(Update update) {
        return Optional.ofNullable(update.getMessage())
                .or(() -> Optional.ofNullable(update.getChannelPost()))
                .orElse(null);
    }
}
