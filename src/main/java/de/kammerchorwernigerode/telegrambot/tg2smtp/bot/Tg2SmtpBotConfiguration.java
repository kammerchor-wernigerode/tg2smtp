package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.LocaleResolver;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.MediaDownloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.MediaReference;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.model.Downloader;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.AuthorizedLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.FilteringNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
class Tg2SmtpBotConfiguration implements Configurer, EnvironmentAware {

    @Setter
    private Environment environment;

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
    public LongPollingBot tg2SmtpBot(Tg2SmtpBotProperties properties, NotificationService notificationService,
                                     TelegramMessageTranslator translator) {
        Tg2SmtpBot tg2SmtpBot = new Tg2SmtpBot(properties, notificationService, translator);
        return new AuthorizedLongPollingBot(tg2SmtpBot, new ChatIdAuthorizer(properties.getChatId()), tg2SmtpBot);
    }

    @Bean
    public AbsSender absSender(Tg2SmtpBotProperties properties) {
        return new DefaultAbsSender(new DefaultBotOptions()) {
            @Override
            public String getBotToken() {
                return properties.getBot().getToken();
            }
        };
    }

    @Bean
    public Downloader<MediaReference> telegramFileDownloader(Tg2SmtpBotProperties properties, AbsSender absSender) {
        return new MediaDownloader(properties, absSender::execute);
    }

    @Bean
    public LocationUrlResolver locationUrlResolver() {
        return new GoogleMapsLocationUrlResolver();
    }

    @Bean
    public LocaleResolver localeResolver() {
        return message -> Locale.getDefault();
    }

    @Override
    public void addPrinters(PrinterRegistry registry) {
        if (hasProfile("debug")) {
            registry.addPrinter(Object.class, new ToStringPrinter());
        }
    }

    private boolean hasProfile(String profile) {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(byEqualityIgnoringCasing(profile));
    }

    private static Predicate<String> byEqualityIgnoringCasing(String profile) {
        return self -> self.equalsIgnoreCase(profile);
    }
}
