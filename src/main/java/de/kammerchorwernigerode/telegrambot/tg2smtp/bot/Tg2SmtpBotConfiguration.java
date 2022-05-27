package de.kammerchorwernigerode.telegrambot.tg2smtp.bot;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.app.TelegramMessageTranslator;
import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.longpolling.AuthorizedLongPollingBot;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.FilteringNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.MessageCompositor;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.FreemarkerRenderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.telegram.infrastructure.TimestampPrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.format.Printer;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
@RequiredArgsConstructor
class Tg2SmtpBotConfiguration implements Configurer {

    private final ListableBeanFactory beanFactory;

    @Bean
    public Renderer renderer(freemarker.template.Configuration configuration, PrinterService printerService) {
        return new FreemarkerRenderer(configuration, printerService);
    }

    @Bean
    @Profile("!debug")
    public NotificationService notificationService(Tg2SmtpBotProperties properties,
                                                   JavaMailSender javaMailSender,
                                                   MailProperties mailProperties,
                                                   Renderer renderer) {
        EmailNotificationService notificationService = new EmailNotificationService(properties,
                javaMailSender,
                mailProperties,
                renderer);
        return new FilteringNotificationService(notificationService, new EmptyMessageFilter(renderer));
    }

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

    @Bean
    public ThrowingFunction<GetFile, File, TelegramApiException> methodExtractor(AbsSender absSender) {
        return absSender::execute;
    }

    @Bean("notificationDelimiter")
    public CharSequence notificationDelimiter() {
        return "\n";
    }

    @Primary
    @Bean
    public Printer<Instant> instantPrinter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        return new TimestampPrinter(formatter);
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        beanFactory.getBeansOfType(NotificationFactory.class).values()
                .forEach(registry::addNotificationFactory);
    }
}
