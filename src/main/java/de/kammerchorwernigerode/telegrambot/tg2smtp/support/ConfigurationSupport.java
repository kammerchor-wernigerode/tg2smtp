package de.kammerchorwernigerode.telegrambot.tg2smtp.support;

import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.ConfigurablePrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.NullSafePrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.app.PrinterService;
import de.kammerchorwernigerode.telegrambot.tg2smtp.format.model.PrinterRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.infrastructure.ConfigurableNotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryProvider;
import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.model.NotificationFactoryRegistry;
import org.springframework.context.annotation.Bean;

/**
 * Configuration main class for the application's Java config.
 *
 * @author Vincent Nadoll
 */
public class ConfigurationSupport {

    @Bean
    public PrinterService printerService() {
        ConfigurablePrinterService printerService = new ConfigurablePrinterService();
        addPrinters(printerService);
        return new NullSafePrinterService(printerService);
    }

    protected void addPrinters(PrinterRegistry registry) {
    }

    @Bean
    public NotificationFactoryProvider notificationFactoryProvider() {
        ConfigurableNotificationFactoryProvider factoryProvider = new ConfigurableNotificationFactoryProvider();
        addNotificationFactories(factoryProvider);
        return factoryProvider;
    }

    protected void addNotificationFactories(NotificationFactoryRegistry registry) {
    }
}
