package de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure;

import de.kammerchorwernigerode.telegrambot.tg2smtp.common.ThrowingFunction;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.FilteringNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.email.EmailNotificationService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactory;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.Renderer;
import de.kammerchorwernigerode.telegrambot.tg2smtp.spi.Configurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Tg2SmtpMessageTypeProperties.class, Tg2SmtpNotificationProperties.class})
@RequiredArgsConstructor
public class NotificationConfiguration implements Configurer {

    private final ListableBeanFactory beanFactory;

    @Bean
    @Profile("!debug")
    public NotificationService notificationService(Tg2SmtpNotificationProperties properties,
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
    public ThrowingFunction<GetFile, File, TelegramApiException> methodExtractor(DefaultAbsSender absSender) {
        return absSender::execute;
    }

    @Bean
    public Function<File, String> pathExtractor(DefaultAbsSender absSender) {
        return file -> file.getFileUrl(absSender.getBotToken());
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
