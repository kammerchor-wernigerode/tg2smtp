package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.EmailNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.bot.infrastructure.Tg2SmtpBotProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.FilteringNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.FreemarkerRenderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.support.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpBotProperties.class})
@RequiredArgsConstructor
public class NotificationConfiguration implements Configurer {

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
    public ThrowingFunction<GetFile, File, TelegramApiException> methodExtractor(AbsSender absSender) {
        return absSender::execute;
    }

    @Bean
    public Function<File, String> pathExtractor(Tg2SmtpBotProperties properties) {
        return file -> file.getFileUrl(properties.getBot().getToken());
    }

    @Bean("notificationDelimiter")
    public CharSequence notificationDelimiter() {
        return "\n";
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        beanFactory.getBeansOfType(NotificationFactory.class).values()
                .forEach(registry::addNotificationFactory);
    }
}
