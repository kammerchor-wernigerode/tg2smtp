package de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.Tg2SmtpBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.AuthorizedLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MessageCompositor;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
@RequiredArgsConstructor
class Tg2SmtpBotConfiguration {

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        return new DefaultBotOptions();
    }

    @Bean
    public LongPollingBot tg2SmtpBot(DefaultBotOptions botOptions, Tg2SmtpBotProperties properties,
                                     NotificationService notificationService, TelegramMessageTranslator translator,
                                     MessageCompositor messageCompositor) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(botOptions, properties, notificationService, translator, messageCompositor);
        return new AuthorizedLongPollingBot(tg2SmtpBot, new ChatIdAuthorizer(properties.getChatId()), tg2SmtpBot);
    }

    @Bean
    public AbsSender absSender(DefaultBotOptions botOptions, Tg2SmtpBotProperties properties) {
        return new DefaultAbsSender(botOptions) {
            @Override
            public String getBotToken() {
                return properties.getBot().getToken();
            }
        };
    }
}
