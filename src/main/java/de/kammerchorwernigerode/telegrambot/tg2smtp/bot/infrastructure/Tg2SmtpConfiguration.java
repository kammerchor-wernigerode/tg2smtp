package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationUrlResolver;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.TextPrinter;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer.nullSafe;

/**
 * SPI-{@link Configurer} implementation that adapts the Telegram Bot API for then Tg2SMTP Bot domain.
 *
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class Tg2SmtpConfiguration implements Configurer {

    private final LocationUrlResolver locationUrlResolver;
    private final freemarker.template.Configuration configuration;

    @Override
    public void addPrinters(PrinterRegistry registry) {
        registry.addPrinter(String.class, textPrinter());
        registry.addPrinter(Poll.class, pollPrinter());
        registry.addPrinter(Location.class, locationPrinter());
    }

    private Printer<String> textPrinter() {
        return nullSafe(new TextPrinter());
    }

    private Printer<Poll> pollPrinter() {
        return nullSafe(new PollPrinter());
    }

    private Printer<Location> locationPrinter() {
        return nullSafe(new LocationPrinter(locationUrlResolver));
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        registry.addNotificationFactory(String.class, textNotificationFactory());
        registry.addNotificationFactory(Location.class, locationNotificationFactory());
    }

    private TextNotificationFactory textNotificationFactory() {
        return new TextNotificationFactory();
    }

    private LocationNotificationFactory locationNotificationFactory() {
        return new LocationNotificationFactory(configuration, new LocationPrinter(locationUrlResolver));
    }
}
