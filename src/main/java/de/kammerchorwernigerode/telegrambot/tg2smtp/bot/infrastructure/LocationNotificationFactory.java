package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter;
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
import org.telegram.telegrambots.meta.api.objects.Location;

/**
 * {@link NotificationFactory} that creates templated {@link FreemarkerNotification}s from Telegram {@link Location}
 * messages.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
public class LocationNotificationFactory implements NotificationFactory<Location> {

    private final @NonNull Configuration configuration;
    private final @NonNull LocationPrinter printer;

    @Override
    public Notification create(@NonNull Location message, @NonNull Metadata metadata) {
        TemplateBuilder templateBuilder = new TemplateBuilder("location.ftl").locale(metadata.getLocale());

        Notification notification = new FreemarkerNotification(templateBuilder, configuration)
                .with("printer", printer)
                .with("model", message);
        return new MetadataHeadedNotificationDecorator(metadata, notification);
    }
}
