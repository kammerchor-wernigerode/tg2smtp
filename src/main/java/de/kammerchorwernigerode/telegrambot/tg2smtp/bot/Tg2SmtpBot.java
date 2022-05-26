package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MessageCompositor;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationComposite;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * {@link org.telegram.telegrambots.meta.generics.TelegramBot} that forwards Telegram messages.
 *
 * @author Vincent Nadoll
 */
@Slf4j
public class Tg2SmtpBot extends TelegramLongPollingBot {

    private final Tg2SmtpBotProperties properties;
    private final NotificationService notificationService;
    private final TelegramMessageTranslator translator;
    private final MessageCompositor messageCompositor;

    public Tg2SmtpBot(DefaultBotOptions options,
                      Tg2SmtpBotProperties properties,
                      NotificationService notificationService,
                      TelegramMessageTranslator translator,
                      MessageCompositor messageCompositor) {
        super(options);
        this.properties = properties;
        this.notificationService = notificationService;
        this.translator = translator;
        this.messageCompositor = messageCompositor;
    }

    @Override
    public String getBotUsername() {
        return properties.getBot().getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getBot().getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        onUpdatesReceived(singletonList(update));
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        Notification notification = updates.stream()
                .map(this::extractMessage)
                .filter(this::nonNull)
                .peek(this::logDelivered)
                .map(translator::translate)
                .filter(optional -> {
                    if (optional.isPresent()) return true;
                    if (log.isDebugEnabled()) log.debug("Message is empty, noting to do");
                    return false;
                })
                .map(Optional::get)
                .collect(collectingAndThen(toList(),
                        notifications -> new NotificationComposite(notifications, messageCompositor)));

        notificationService.send(notification);
    }

    private boolean nonNull(Message message) {
        if (null == message) {
            if (log.isDebugEnabled()) log.debug("Message is empty, skipping one");
            return false;
        }

        return true;
    }

    private void logDelivered(Message message) {
        if (log.isTraceEnabled()) log.trace("Received message from chat w/ chatId={}", message.getChatId());
    }

    @Nullable
    private Message extractMessage(Update update) {
        return UpdateUtils.extractMessage(update).orElse(null);
    }
}
