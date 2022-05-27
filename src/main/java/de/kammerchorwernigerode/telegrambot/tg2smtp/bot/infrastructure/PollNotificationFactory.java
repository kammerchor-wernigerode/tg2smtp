package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MetadataHeadedNotificationDecorator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.Notification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FreemarkerNotification;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.TemplateBuilder;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.model.Metadata;
import freemarker.template.Configuration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Poll}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class PollNotificationFactory implements NotificationFactory<Poll> {

    private final @NonNull Configuration configuration;
    private final @NonNull PollPrinter printer;

    @Override
    public Notification create(@NonNull Poll message, @NonNull Metadata metadata) {
        TemplateBuilder template = new TemplateBuilder("poll.ftl").locale(metadata.getLocale());

        Notification notification = new FreemarkerNotification(template, configuration)
                .with("printer", printer)
                .with("model", message);
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }
}
