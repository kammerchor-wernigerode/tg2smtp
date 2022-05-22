package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.Locale;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Poll}
 * messages.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class PollNotificationFactory implements NotificationFactory<Poll> {

    private final @NonNull Configuration configuration;
    private final @NonNull PollPrinter printer;

    @Override
    public Notification create(@NonNull Poll message, @NonNull Locale locale) {
        TemplateBuilder template = new TemplateBuilder("poll.ftl").locale(locale);

        return new FreemarkerNotification<>(template, configuration, printer, message);
    }
}
