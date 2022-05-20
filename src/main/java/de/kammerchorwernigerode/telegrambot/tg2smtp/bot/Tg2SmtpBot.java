package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Supplier;

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
    private final PrinterService printerService;

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
        StringBuilder text = new StringBuilder();
        printTo(text, message::getText, message::getPoll);

        if (!StringUtils.hasText(text)) {
            if (log.isDebugEnabled()) log.debug("Text is empty, noting to do");
            return;
        }

        String mailText = text.toString();
        if (!StringUtils.hasText(mailText)) {
            return;
        }

        notificationService.send(() -> mailText);
    }

    @Nullable
    private Message extractMessage(Update update) {
        return UpdateUtils.extractMessage(update).orElse(null);
    }

    private void printTo(StringBuilder builder, Supplier<?>... suppliers) {
        for (Supplier<?> supplier : suppliers) {
            builder.append(printerService.print(supplier.get()));
        }
    }
}
