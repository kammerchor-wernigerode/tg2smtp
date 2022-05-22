package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * {@link org.telegram.telegrambots.meta.generics.TelegramBot} that forwards Telegram messages.
 *
 * @author Vincent Nadoll
 */
@Slf4j
@RequiredArgsConstructor
public class Tg2SmtpBot extends TelegramLongPollingBot {

    private final Tg2SmtpBotProperties properties;
    private final NotificationService notificationService;
    private final TelegramMessageTranslator translator;

    @Override
    public String getBotUsername() {
        return properties.getBot().getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getBot().getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = extractMessage(update);
        if (null == message) {
            if (log.isDebugEnabled()) log.debug("Message is empty, skipping this update={}", update);
            return;
        }

        if (log.isTraceEnabled()) log.trace("Received message from chat w/ chatId={}", message.getChatId());
        Notification notification = translator.translate(message);
        notificationService.send(notification);
    }

    @Nullable
    private Message extractMessage(Update update) {
        return UpdateUtils.extractMessage(update).orElse(null);
    }
}
