package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.LocaleResolver;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.AuthorizedLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.FilteringNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.Locale;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
@RequiredArgsConstructor
class Tg2SmtpBotConfiguration implements Configurer {

    private final ListableBeanFactory beanFactory;

    @Bean
    @Profile("!debug")
    public NotificationService notificationService(Tg2SmtpBotProperties properties,
                                                   JavaMailSender javaMailSender,
                                                   MailProperties mailProperties) {
        EmailNotificationService notificationService = new EmailNotificationService(properties,
                javaMailSender,
                mailProperties);
        return new FilteringNotificationService(notificationService, new EmptyMessageFilter());
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        return new DefaultBotOptions();
    }

    @Bean
    public LongPollingBot tg2SmtpBot(DefaultBotOptions botOptions, Tg2SmtpBotProperties properties,
                                     NotificationService notificationService, TelegramMessageTranslator translator) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(botOptions, properties, notificationService, translator);
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

    @Bean
    public LocaleResolver localeResolver() {
        return message -> Locale.getDefault();
    }

    @Bean
    public ThrowingFunction<GetFile, File, TelegramApiException> methodExtractor(AbsSender absSender) {
        return absSender::execute;
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        beanFactory.getBeansOfType(NotificationFactory.class).values()
                .forEach(registry::addNotificationFactory);
    }
}
