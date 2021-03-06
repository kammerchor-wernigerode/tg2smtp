package de.kammerchorwernigerode.telegrambot.tg2smtp.spi;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterRegistry;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Configurer} that delegates to one or more others.
 *
 * @author Vincent Nadoll
 */
class ConfigurerComposite implements Configurer {

    private final List<Configurer> delegates = new ArrayList<>();

    public void addConfigurers(List<Configurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }

    @Override
    public void addNotificationFactories(NotificationFactoryRegistry registry) {
        delegates.forEach(configurer -> configurer.addNotificationFactories(registry));
    }

    @Override
    public void addPrinters(PrinterRegistry registry) {
        delegates.forEach(configurer -> configurer.addPrinters(registry));
    }
}
