package de.kammerchorwernigerode.telegrambot.tg2smtp.spi;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.ConfigurableNotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.Tg2SmtpMessageTypeProperties;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.infrastructure.ConfigurablePrinterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * Configuration main class for the application's Java config.
 *
 * @author Vincent Nadoll
 */
public class ConfigurationSupport {

    @Bean
    @ConditionalOnBean(Tg2SmtpMessageTypeProperties.class)
    public NotificationFactoryProvider notificationFactoryProvider(Tg2SmtpMessageTypeProperties properties) {
        ConfigurableNotificationFactoryProvider factoryProvider =
                new ConfigurableNotificationFactoryProvider(properties);
        addNotificationFactories(factoryProvider);
        return factoryProvider;
    }

    protected void addNotificationFactories(NotificationFactoryRegistry registry) {
    }

    @Bean
    public PrinterService printerService() {
        ConfigurablePrinterService service = new ConfigurablePrinterService();
        addPrinters(service);
        return service;
    }

    protected void addPrinters(ConfigurablePrinterService service) {
    }
}
