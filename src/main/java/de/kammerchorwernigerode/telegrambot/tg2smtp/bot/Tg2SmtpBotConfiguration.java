package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.FilteringLongPollingBot;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
class Tg2SmtpBotConfiguration {

    @Bean
    public LongPollingBot tg2SmtpBot(Tg2SmtpBotProperties properties, JavaMailSender mailSender, MailProperties mailProperties) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(properties, mailSender, mailProperties);
        return new FilteringLongPollingBot(tg2SmtpBot, new ChatFilter(properties.getChatId()), tg2SmtpBot);
    }
}
