package de.kammerchorwernigerode.telegrambot.tg2smtp.spi;

import de.kammerchorwernigerode.telegrambot.tg2smtp.notification.app.NotificationFactoryRegistry;
import de.kammerchorwernigerode.telegrambot.tg2smtp.print.infrastructure.ConfigurablePrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * A subclass of {@link ConfigurationSupport} that detects and delegates to all beans of type {@link Configurer}
 * allowing them to customize the configuration provided by {@link ConfigurationSupport}.
 *
 * @author Vincent Nadoll
 */
@Configuration(proxyBeanMethods = false)
public class DelegatingConfiguration extends ConfigurationSupport {

    private final ConfigurerComposite configurers = new ConfigurerComposite();

    @Autowired(required = false)
    public void setConfigurers(List<Configurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addConfigurers(configurers);
        }
    }

    @Override
    protected void addNotificationFactories(NotificationFactoryRegistry registry) {
        configurers.addNotificationFactories(registry);
    }

    @Override
    protected void addPrinters(ConfigurablePrinterService service) {
        configurers.addPrinters(service);
    }
}
