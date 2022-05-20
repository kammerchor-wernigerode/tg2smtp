package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.FilteringLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import static de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer.nullSafe;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
class Tg2SmtpBotConfiguration implements Configurer {

    @Bean
    public LongPollingBot tg2SmtpBot(Tg2SmtpBotProperties properties, NotificationService notificationService,
                                     PrinterService printerService) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(properties, notificationService, printerService);
        return new FilteringLongPollingBot(tg2SmtpBot, new ChatFilter(properties.getChatId()), tg2SmtpBot);
    }

    @Override
    public void addPrinters(PrinterRegistry registry) {
        registry.addPrinter(String.class, nullSafe(new TextPrinter()));
        registry.addPrinter(Poll.class, nullSafe(new PollPrinter()));
    }
}
