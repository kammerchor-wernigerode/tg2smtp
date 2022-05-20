package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.Printer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.FilteringLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
class Tg2SmtpBotConfiguration implements Configurer {

    @Bean
    public LongPollingBot tg2SmtpBot(Tg2SmtpBotProperties properties, JavaMailSender mailSender, MailProperties mailProperties,
                                     PrinterService printerService) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(properties, mailSender, mailProperties, printerService);
        return new FilteringLongPollingBot(tg2SmtpBot, new ChatFilter(properties.getChatId()), tg2SmtpBot);
    }

    @Override
    public void addPrinters(PrinterRegistry registry) {
        registry.addPrinter(Poll.class, Printer.nullSafe(new PollPrinter()));
        registry.addPrinter(String.class, new TextPrinter());
    }
}
